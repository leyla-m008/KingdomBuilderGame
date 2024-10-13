package com.example;

public class BoardGraph {
    private String[][] mapMatrix;
    private HexNode[][] hexMatrix;

    public BoardGraph(String[][] matrix) {
        mapMatrix = matrix;
        hexMatrix = new HexNode[mapMatrix.length][mapMatrix[0].length];

        buildGraph();
    }

    public HexNode[][] getMatrix() {return hexMatrix;}

    public void buildGraph() {
        for(int r = 0; r < mapMatrix.length; r++) {
            for(int c = 0; c < mapMatrix[0].length; c++) {
                hexMatrix[r][c] = new HexNode(mapMatrix[r][c], (2 * (r / 10)) + (c / 10));
            }
        }

        for(int r = 0; r < mapMatrix.length; r++) {
            for(int c = 0; c < mapMatrix[0].length; c++) {
                HexNode[] bordering = hexMatrix[r][c].getBordering();

                if(r != 0) {
                    if(c != 19)
                        bordering[0] = hexMatrix[r - 1][c + (r % 2)];
                    if(c == 19)
                        if(r % 2 == 0)
                            bordering[0] = hexMatrix[r - 1][c];
                }
                if(c != hexMatrix[0].length - 1) {
                    bordering[1] = hexMatrix[r][c + 1];
                }
                if(r != hexMatrix[0].length - 1) {
                    if(c != 19)
                        bordering[2] = hexMatrix[r +1 ][c + (r % 2)];
                    if(c == 19)
                        if(r % 2 == 0)
                            bordering[2] = hexMatrix[r + 1][c];
                }
                if(r != hexMatrix[0].length - 1) {
                    if(c != 0)
                        bordering[3] = hexMatrix[r + 1][c - ((r + 1) % 2)];
                    if (c == 0)
                        if((r + 1) % 2 == 0)
                            bordering[3] = hexMatrix[r + 1][c];
                }
                if(c != 0) {
                    bordering[4] = hexMatrix[r][c - 1];
                }
                if(r != 0) {
                    if(c != 0)
                        bordering[5] = hexMatrix[r - 1][c - ((r + 1) % 2)];
                    if (c == 0)
                        if((r + 1) % 2 == 0)
                            bordering[5] = hexMatrix[r - 1][c];
                }
            }
        }
    }

    public String toString() {
        if(hexMatrix == null) {
            return "";
        }

        String str = "";
        for(HexNode[] arr : hexMatrix) {
            for(HexNode hex : arr) {
                str += hex + " ";
            }
            str += "\n";
        }
        return str;
    }
}








