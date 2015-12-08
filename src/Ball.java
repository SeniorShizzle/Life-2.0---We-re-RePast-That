public class Ball {

    public double x;
    public double y;
    public int diameter;

    private double velocityX;
    private double velocityY;

    public Ball(int diameter){
        this.x = 500;
        this.y = 500;

        this.diameter = diameter;

        this.velocityX = Math.random() * 10;
        this.velocityY = -10;
    }

    public void update(){
        // The update loop

        if ((int)(Math.random() * 300) == 5) {
            this.velocityY *= 1;
            this.velocityX *= 1;
        }

        this.velocityY += 0.25; // gravity
        //this.velocityY *= 0.997;  // deceleration
        //this.velocityX *= 0.995;

        if (this.x < 0 || this.x + diameter > 1000){
            this.velocityX *= -0.98; // reverse the x direction
        }

        if (this.y < 0 || this.y + diameter > 1000){
            this.velocityY *= -0.98; // reverse the y direction
        }

        this.x += this.velocityX;
        this.y += this.velocityY;

    }


}
