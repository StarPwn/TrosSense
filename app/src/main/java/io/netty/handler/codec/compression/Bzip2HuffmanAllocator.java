package io.netty.handler.codec.compression;

/* loaded from: classes4.dex */
final class Bzip2HuffmanAllocator {
    private static int first(int[] array, int i, int nodesToMove) {
        int length = array.length;
        int k = array.length - 2;
        while (i >= nodesToMove && array[i] % length > i) {
            k = i;
            i -= (i - i) + 1;
        }
        int i2 = Math.max(nodesToMove - 1, i);
        while (k > i2 + 1) {
            int temp = (i2 + k) >>> 1;
            if (array[temp] % length > i) {
                k = temp;
            } else {
                i2 = temp;
            }
        }
        return k;
    }

    private static void setExtendedParentPointers(int[] array) {
        int topNode;
        int temp;
        int length = array.length;
        array[0] = array[0] + array[1];
        int headNode = 0;
        int topNode2 = 2;
        for (int tailNode = 1; tailNode < length - 1; tailNode++) {
            if (topNode2 >= length || array[headNode] < array[topNode2]) {
                topNode = array[headNode];
                array[headNode] = tailNode;
                headNode++;
            } else {
                topNode = array[topNode2];
                topNode2++;
            }
            if (topNode2 >= length || (headNode < tailNode && array[headNode] < array[topNode2])) {
                int topNode3 = array[headNode];
                temp = topNode + topNode3;
                array[headNode] = tailNode + length;
                headNode++;
            } else {
                temp = topNode + array[topNode2];
                topNode2++;
            }
            array[tailNode] = temp;
        }
    }

    private static int findNodesToRelocate(int[] array, int maximumLength) {
        int currentNode = array.length - 2;
        for (int currentDepth = 1; currentDepth < maximumLength - 1 && currentNode > 1; currentDepth++) {
            currentNode = first(array, currentNode - 1, 0);
        }
        return currentNode;
    }

    private static void allocateNodeLengths(int[] array) {
        int firstNode = array.length - 2;
        int nextNode = array.length - 1;
        int currentDepth = 1;
        int availableNodes = 2;
        while (availableNodes > 0) {
            int lastNode = firstNode;
            firstNode = first(array, lastNode - 1, 0);
            int i = availableNodes - (lastNode - firstNode);
            while (i > 0) {
                array[nextNode] = currentDepth;
                i--;
                nextNode--;
            }
            int i2 = lastNode - firstNode;
            availableNodes = i2 << 1;
            currentDepth++;
        }
    }

    private static void allocateNodeLengthsWithRelocation(int[] array, int nodesToMove, int insertDepth) {
        int firstNode = array.length - 2;
        int nextNode = array.length - 1;
        int currentDepth = insertDepth != 1 ? 1 : 2;
        int nodesLeftToMove = insertDepth == 1 ? nodesToMove - 2 : nodesToMove;
        int availableNodes = currentDepth << 1;
        while (availableNodes > 0) {
            int lastNode = firstNode;
            firstNode = firstNode <= nodesToMove ? firstNode : first(array, lastNode - 1, nodesToMove);
            int offset = 0;
            if (currentDepth >= insertDepth) {
                offset = Math.min(nodesLeftToMove, 1 << (currentDepth - insertDepth));
            } else if (currentDepth == insertDepth - 1) {
                offset = 1;
                if (array[firstNode] == lastNode) {
                    firstNode++;
                }
            }
            int i = availableNodes - ((lastNode - firstNode) + offset);
            while (i > 0) {
                array[nextNode] = currentDepth;
                i--;
                nextNode--;
            }
            nodesLeftToMove -= offset;
            availableNodes = ((lastNode - firstNode) + offset) << 1;
            currentDepth++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void allocateHuffmanCodeLengths(int[] array, int maximumLength) {
        switch (array.length) {
            case 1:
                break;
            case 2:
                array[1] = 1;
                break;
            default:
                setExtendedParentPointers(array);
                int nodesToRelocate = findNodesToRelocate(array, maximumLength);
                if (array[0] % array.length >= nodesToRelocate) {
                    allocateNodeLengths(array);
                    return;
                } else {
                    int insertDepth = maximumLength - (32 - Integer.numberOfLeadingZeros(nodesToRelocate - 1));
                    allocateNodeLengthsWithRelocation(array, nodesToRelocate, insertDepth);
                    return;
                }
        }
        array[0] = 1;
    }

    private Bzip2HuffmanAllocator() {
    }
}
