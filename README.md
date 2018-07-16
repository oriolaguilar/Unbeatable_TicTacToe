# Unbeatable-TicTacToe
An unbeatable TicTacToe made in order to practice Java skills.

Hi I am Oriol and if you are reading this is because you want to know how I made this program.

The basic idea of this game (back-end) is a 3x3 int matrix initialized by 0's and where the 1 are your pieces and the -1 the CPU's pieces (your rival).

The front-end is made with Java's Graphic Libraries using simple objects such us GLines and GOvals.

As you can see down bellow, the main function is simple and in this document, I'm going to explain moveCPU() method because it's the most signifiant part of the project. 
   
        initialize();
        
        while(!TTT() && !draw()){
            
            movePlayer();
            
            moveCPU();
        }
        
        endOfTheProgram();
        

The method moveCPU() consists on this basic structure: 

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
    
First of all checks if it's the first move because depending on what move does the player, the CPU will do one or another.
If it's not the first move will go directly to the else{ ... }, there it would do the intelligentMove(), except that cornersSituation() == true, a situation that must be treated individually.

The method intelligentMove(), consists basically on a priority chain made with if statements.

    private void intelligentMove(){
    
        if(canWinOrBlock(2*CPU)){      
            int pos = moveToWinOrBlock(2*CPU, CPU);
            drawToBoard(pos, Color.blue);
        }else if(canWinOrBlock(2*PLAYER)){ 
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
    
For example, when it's the time to move for the CPU, first of all will check if he can connect 3 (canWinOrBlock(2*CPU)), if not he'll check if he can block your connect 3 (canWinOrBlock(2*PLAYER)), if still not, it will move to a position where it can do a double connect 2, if this is not possible and you are going to do the double connect 2 it will block it. The boolean methods stillNoMove() are used because it's an efficient way. If nothing of this happens it just move to a random position.
The canDoDouble() methods work modifing the board adding a piece (1 or -1) and analyising the consequences.



