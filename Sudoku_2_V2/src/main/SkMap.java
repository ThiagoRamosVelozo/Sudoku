package main;

import java.util.Random;

class SkMap {
    
    public static String colStr(int[][] skgen, int col){
        
        String cstr = "";
        for (int[] skgen1 : skgen) { cstr += "" + skgen1[col]; }
        return cstr.replace("0", "");
        
    }
    
    public static String rowStr(int[][] skgen, int row){
        
        String rstr = "";
        for (int e: skgen[row]) { rstr += "" + e; }
        return rstr.replace("0", "");
        
    }
    
    public static String frmStr(int[][] skgen, int row, int col){
        
        String fstr = "";
        
        int rowfrm = row/3, colfrm = col/3, ifrm, jfrm;
        
        for (int i = 0; i < skgen.length && fstr.length() < 9; i++){
            
            for (int j = 0; j < skgen[0].length && fstr.length() < 9; j++){
            
                ifrm = i/3; jfrm = j/3;
            
                if (ifrm == rowfrm && jfrm == colfrm) {
                
                    fstr += ""+skgen[i][j];
                
                }
            }
        }
        
        return fstr.replace("0", "");
        
    }
    
    public static int[][] gen(int x, int y, int min, int max){
        
        int[][] skgen = new int[x][y];
        Random random = new Random();
        String neighb; int errors = 0;
        
        for (int i = 0; i < x; i++){ for (int j = 0; j < y; j++){
            
            skgen[i][j] = 0;
            
            String nums = "123456789";
            
            neighb = rowStr(skgen, i) + colStr(skgen, j) + frmStr(skgen, i, j);
            
            for (char ch: neighb.toCharArray()) {
                
                if (nums.contains(String.valueOf(ch))){
                    nums = nums.replaceAll(String.valueOf(ch), "");
                }
                
            }
            try{ skgen[i][j] = Integer.parseInt("" + nums.charAt( random.nextInt( nums.length() ) ) ); } catch(Exception e) { i--; errors++; }
            if (errors > 50){ return new int[1][1]; }
            
        }}
        
        return skgen;
        
    }
    
    public static boolean[][] sponge(int x, int y){
        
        boolean[][] sksponge = new boolean[x][y]; Random random = new Random();
        
        for (int i = 0; i < x; i++){ for (int j = 0; j < y; j++){
            
            sksponge[i][j] = random .nextInt(9) > 1;
            
        }}
        
        return sksponge;
        
    }
    
}
