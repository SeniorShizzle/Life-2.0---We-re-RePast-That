public class Ball {

    public int x;
    public int y;
    public int radius;

    private double velocityX;
    private double velocityY;

    public Ball(int radius){
        this.x = 500;
        this.y = 500;

        this.radius = radius;

        this.velocityX = 1;
        this.velocityY = 10;
    }

    public void update(){
        // The update loop

        this.velocityY -= 0.09;


        if (this.x + radius < 0 || this.x + radius > 1000) this.velocityX *= -1; // reverse the x direction
        if (this.y + radius < 0 || this.y + radius > 1000) this.velocityY *= -1; // reverse the y direction

        this.x += this.velocityX;
        this.y += this.velocityY;

    }


}
