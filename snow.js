class SnowAnimation {
    constructor(canvasId, options = {}) {
        // Get canvas and context
        this.canvas = document.getElementById(canvasId);
        this.ctx = this.canvas.getContext('2d');
        
        // Set canvas size to window size or specified dimensions
        //this.canvas.width = options.width || window.innerWidth;
        //this.canvas.height = options.height || window.innerHeight;
        
        // Initialize properties
        this.snows = Math.min(options.snows || 100, 500);
        this.threadSleep = Math.min(Math.max(options.threadSleep || 80, 10), 1000);
        this.wind = 0;
        this.backgroundImage = new Image();
        this.snowflakes = [];
        
        // Initialize snowflakes
        this.initSnowflakes();
        
        // Set background image if provided
        if (options.backgroundImage) {
            this.backgroundImage.src = options.backgroundImage;
            this.backgroundImage.onload = () => this.start();
        } else {
            this.start();
        }
    }

    initSnowflakes() {
        for (let i = 0; i < this.snows; i++) {
            this.snowflakes.push({
                x: Math.random() * this.canvas.width,
                y: Math.random() * this.canvas.height
            });
        }
    }

    updateWind() {
        const randomValue = Math.floor(Math.random() * 100);
        if (randomValue <= 2 && randomValue >= -2) {
            this.wind = randomValue;
        }
    }

    drawBackSnow() {
        this.ctx.fillStyle = 'white';
        
        this.snowflakes.forEach(snowflake => {
            // Draw snowflake
            this.ctx.fillRect(snowflake.x, snowflake.y, 1, 1);
            
            // Update position
            snowflake.x += (Math.random() * 2 - 1) + this.wind;
            snowflake.y += (Math.random() * 6 + 5) / 5 + 1;
            
            // Handle boundaries
            if (snowflake.x >= this.canvas.width) {
                snowflake.x = 0;
            }
            if (snowflake.x < 0) {
                snowflake.x = this.canvas.width - 1;
            }
            if (snowflake.y >= this.canvas.height || snowflake.y < 0) {
                snowflake.x = Math.random() * this.canvas.width;
                snowflake.y = 0;
            }
        });
        
        this.updateWind();
    }

    draw() {
        // Clear canvas
        this.ctx.fillStyle = 'black';
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
        
        // Draw background image if exists
        if (this.backgroundImage.complete) {
            this.ctx.drawImage(this.backgroundImage, 0, 0);
        }
        
        // Draw snow
        this.drawBackSnow();
    }

    start() {
        if (!this.animationFrame) {
            const animate = () => {
                this.draw();
                this.animationFrame = setTimeout(() => {
                    requestAnimationFrame(animate);
                }, this.threadSleep);
            };
            animate();
        }
    }

    stop() {
        if (this.animationFrame) {
            clearTimeout(this.animationFrame);
            this.animationFrame = null;
        }
    }
}
