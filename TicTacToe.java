
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;


/**
 *
 * @author oriolaguilar
 */
public class TicTacToe extends GraphicsProgram {
    
    private static int[][] board = new int[3][3];
    
    private static final int PLAYER = 1;
    private static final int CPU = -1;
    
    @Override
    public void run(){
   
        initialize();
        
        while(!TTT() && !draw()){
            
            movePlayer();
            
            moveCPU();
        }
        
        endOfTheProgram();
        
    }
    
    private void initialize(){  
        board = new int[3][3];
        squareCanvas();
        drawLines();
    }
    
    private void squareCanvas() {
        setSize(new Dimension(624, 681));
    }
    
    private void drawLines() {
        
        double h = 600;
        double w = 600;
        
        for (int i = 0; i<3 ; i++){
            GLine line = new GLine(0, h-h*i/3, w, h-h*i/3);
            add(line);
        }
        for (int i = 0; i<3 ; i++){
            GLine line = new GLine(h-h*i/3, 0, h-h*i/3, h);
            add(line);
        }
    }
    

    
    private boolean TTT(){          
        
        int[][] c = allTheCombs();
        
        for(int i = 0; i<c.length; i++){
            if(Math.abs(checkings(c[i][0],c[i][1],c[i][2])) == 3) return true;
        }
        
        return false;
    }
    
    
    private boolean draw(){ 
        for(int i = 0 ; i < 3 ; i++){
            for (int j = 0 ; j < 3 ; j++){
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }
    
    private int checkings(int a, int b, int c){
        return intOfPosition(a)+intOfPosition(b)+intOfPosition(c);
    }
    
    private int intOfPosition(int pos){
        int fila = pos/3;
        int columna = pos-(fila*3);
        return board[fila][columna];
    }

    private void movePlayer() {
        int pos;
        do{
            pos = coordsToPos();
        }while(pos<0 || pos>8 || (intOfPosition(pos) != 0));
       
        posToBoard(pos, PLAYER);
        drawToBoard(pos, Color.red);
        
    }
    
    private int coordsToPos(){
 
        waitForClick();
        Point point = MouseInfo.getPointerInfo().getLocation();
        int x = point.x;
        int y = point.y;
        
        return coordsToPos(x, y);
    }
    private int coordsToPos(int x, int y){
        
        
        if((x>000 && x<227)&&(y>0 && y<307)) return 0;
        else if((x>227 && x<427)&&(y>0 && y<307)) return 1;
        else if((x>427 && x<627)&&(y>0 && y<307)) return 2;
        else if((x>000 && x<227)&&(y>307 && y<507)) return 3;
        else if((x>227 && x<427)&&(y>307 && y<507)) return 4;
        else if((x>427 && x<627)&&(y>307 && y<507)) return 5;
        else if((x>000 && x<227)&&(y>507 && y<707)) return 6;
        else if((x>227 && x<427)&&(y>507 && y<707)) return 7;
        else if((x>427 && x<627)&&(y>507 && y<707)) return 8;
        
        return 0;
    }
    private void drawToBoard(int pos, Color color){
        
        int j = (pos/3);
        int i = pos - (j*3);
                
        GOval piece = new GOval(getHeight()* (i+1)/3 - getHeight()/6 - 50,getWidth()* (j+1)/3 - getWidth()/6 - 50, 100, 100);
        
        piece.setFilled(true);
        piece.setFillColor(color);
        add(piece);
    }

    private void posToBoard(int pos, int player) {
        int row = pos/3;
        int column = pos-(row*3);
        board[row][column] = player;
    }
    
    private void moveCPU() {
        
        pause(300);
        
        if(lastMove()){
            //despite ugly, easiest way to do it.
        }else if(firstMove()){
            firstMoveSituation();   
        }else{
            if(cornersSituation()){
                doCornersSituation();
            }else{
                intelligentMove();
            }
        }        
    }

    private boolean firstMove() {
        int numsOf0 = 0;
        for (int i = 0; i<3 ; i++){
            for (int j = 0 ; j<3 ; j++){
                if (board[i][j] == 0) numsOf0++ ;
            }
        }
        return numsOf0 == 8;
    }
    private void firstMoveSituation() {
        if(playerHasMovedCenter()){
            board[0][0] = CPU;
            drawToBoard(0, Color.blue);
        }else if (playerHasMovedCorners()){
            board[1][1] = CPU;
            drawToBoard(4, Color.blue);
        }else{
            playNextToIt();
        }
    }

    private boolean playerHasMovedCenter() {
        return board[1][1] == PLAYER;
    }
    
    private boolean playerHasMovedCorners(){
        return board[0][0] == PLAYER || 
               board[0][2] == PLAYER ||
               board[2][0] == PLAYER || 
               board[2][2] == PLAYER;
    }
    
    private boolean cornersSituation(){
        return ((count(1) == 2 && count(-1) == 1) && 
               ((board[0][0] == 1 && board[2][2] == 1) || 
                (board[2][0] == 1 && board[0][2] == 1)));
    }
    
    private void doCornersSituation(){                    
        board[1][0] = CPU;
        drawToBoard(3, Color.blue);
    }
    
    private void intelligentMove(){
        if(canWinOrBlock(2*CPU)){      
            int pos = moveToWinOrBlock(2*CPU, CPU);
            drawToBoard(pos, Color.blue);
        }else if(canWinOrBlock(2*PLAYER)){ //JUGADOR
            int pos = moveToWinOrBlock(2*PLAYER, PLAYER);
            drawToBoard(pos, Color.blue);
        }else{
            canDoDouble(CPU);
        }
        if(stillNoMove()){
            canDoDouble(PLAYER);
        }
        if(stillNoMove()){
            int pos = moveNextToOnePiece();
            drawToBoard(pos, Color.blue);
        }
    }
    
    private void playNextToIt(){
        int pos;
        
        if (board[0][1] == 1) pos = 7;
        else if (board[1][0] == 1) pos = 5;
        else if (board[2][1] == 1) pos = 1;
        else pos = 3;
        
        posToBoard(pos, CPU);
        drawToBoard(pos, Color.blue);
    }

    private boolean canWinOrBlock(int n) {                
        int[][] c = allTheCombs();
        
        for(int i = 0; i<c.length; i++){
            if(checkings(c[i][0],c[i][1],c[i][2]) == n) return true;
        }
        
        return false;
    }

    private int moveToWinOrBlock(int goal, int m) {
        
        int[][] c = allTheCombs();
        
        for(int i = 0; i<c.length; i++){
            if(checkings(c[i][0],c[i][1],c[i][2]) == goal){
                if(intOfPosition(c[i][0])!= m){
                    posToBoard(c[i][0], CPU);
                    return c[i][0];
                }else if(intOfPosition(c[i][1])!= m){
                    posToBoard(c[i][1], CPU);
                    return c[i][1];
                }else{
                    posToBoard(c[i][2], CPU);
                    return c[i][2];
                }
            }
        }        
    return 0;
    }
    private void canDoDouble(int p){
        
        outerloop:
        for(int i = 0; i<3 ; i++){
            for(int j = 0; j<3 ; j++){
                if (board[i][j] == 0){
                    whatWouldHappen(i, j, p);
                    if(board[i][j] == CPU) break outerloop;
                }
            }
        }
        
    }

    
    private void whatWouldHappen(int i, int j, int p){
        board[i][j] = p;
        if (possibleTTT()>1){
            board[i][j] = CPU;
            drawToBoard(i*3+j, Color.blue);
        }else{
            board[i][j] = 0;
        }
    }
    
    private int possibleTTT(){
        int num = 0;
        int[][] c = allTheCombs();
        for(int i = 0; i < c.length ; i++){
            if (Math.abs(checkings(c[i][0],c[i][1],c[i][2])) == 2) num++; 
        }
        return num;
    }
    private boolean stillNoMove(){
        return count(PLAYER)>count(CPU);
    }
    
    private int count(int x){
        int num = 0;
        for (int i = 0; i<3 ; i++){
            for (int j = 0; j<3 ;j++){
                if (board[i][j] == x) num++;
            }
        }
        return num;
    }

    private int moveNextToOnePiece() {
        int posToMove;
        do{
            posToMove = random();
        }while (intOfPosition(posToMove)!=0);
        
        posToBoard(posToMove, CPU);
        return posToMove;
    }
    
    public int random(){
        RandomGenerator rgen= RandomGenerator.getInstance();
        return	rgen.nextInt(0,	8);
        
    }

    private boolean lastMove() {
        int sum = 0;
        for(int i = 0; i<3 ;i++){
            for (int j = 0; j<3 ;j++){
                if (board[i][j] == 0) sum++;
            }
        }
        return sum == 0;
    }

    private void endOfTheProgram() {
        
        theResult();
        theRestart();
        
    }
    
    private void restart() {
        GRect cover = new GRect(-1, -1, 700, 700);
        cover.setFilled(true);
        cover.setFillColor(Color.WHITE);
        add(cover);
        run();
    }

    private void restartButton() {
        drawToBoard(4, Color.YELLOW);
        GLabel restart = new GLabel("RESTART", 275, 305);
        add(restart);
    }

    private boolean restartGame() {
        
        waitForClick();
        Point punto = MouseInfo.getPointerInfo().getLocation();
        int x = punto.x;
        int y = punto.y;
        
        return ((x>227 && x<427)&&(y>307 && y<507));
    }
    
    private void theRestart() {
        while(true){
            pause(500);
            restartButton();
            if(restartGame()){
                restart();
            }
        }
    }
    
    private void theResult() {
        pause(500);
        drawToBoard(1, Color.YELLOW);
        
        if(TTT()){
            showResult("YOU LOST");
        }else{
            showResult("YOU DRAW");
        }
    }
    
    private void showResult(String stat) {
        
        GLabel statement = new GLabel(stat, 271, 105);
        add(statement);
    }  

    private int[][] allTheCombs(){ //All the possible combinations to do a TicTacToe based on the position
        int[][] x ={{0, 1, 2}, 
                    {3, 4, 5}, 
                    {6, 7, 8}, 
                    {0, 3, 6}, 
                    {1, 4, 7},
                    {2, 5, 8},
                    {0, 4, 8},
                    {2, 4, 6}};
        return x;                       
    }
}
