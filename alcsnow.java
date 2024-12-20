import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.ImageObserver;
import java.util.Random;

public class alcsnow extends Applet implements Runnable {
   Thread mainThread;
   Image offScrn;
   Graphics offGrph;
   Random rand;
   int stopFlag;
   long stopTime;
   int[] snowX;
   int[] snowY;
   int snows;
   int wind;
   int threadSleep;
   Dimension dim;
   Image[] gAlc;
   MediaTracker mt;

   public void init() {
      this.rand = new Random();
      this.dim = this.size();
      this.offScrn = this.createImage(this.dim.width, this.dim.height);
      this.offGrph = this.offScrn.getGraphics();
      String var2 = this.getParameter("snows");
      if (var2 == null) {
         this.snows = 100;
      } else {
         this.snows = Integer.valueOf(var2);
      }

      if (this.snows > 500) {
         this.snows = 500;
      }

      var2 = this.getParameter("threadsleep");
      if (var2 == null) {
         this.threadSleep = 80;
      } else {
         this.threadSleep = Integer.valueOf(var2);
      }

      if (this.threadSleep < 10) {
         this.threadSleep = 10;
      } else if (this.threadSleep > 1000) {
         this.threadSleep = 1000;
      }

      this.snowX = new int[this.snows];
      this.snowY = new int[this.snows];

      for(int var1 = 0; var1 < this.snows; ++var1) {
         this.snowX[var1] = this.rand.nextInt() % (this.dim.width / 2) + this.dim.width / 2;
         this.snowY[var1] = this.rand.nextInt() % (this.dim.height / 2) + this.dim.height / 2;
      }

      var2 = this.getParameter("graphic");
      if (var2 == null) {
         var2 = this.getParameter("graph");
         if (var2 == null) {
            var2 = this.getParameter("grph");
            if (var2 == null) {
               var2 = "tento1.gif";
            }
         }
      }

      this.mt = new MediaTracker(this);
      this.gAlc = new Image[1];
      this.gAlc[0] = this.getImage(this.getDocumentBase(), var2);
      this.mt.addImage(this.gAlc[0], 0);

      try {
         this.mt.waitForID(0);
      } catch (InterruptedException var3) {
         return;
      }

      this.stopFlag = 0;
   }

   public void start() {
      if (this.mainThread == null) {
         this.mainThread = new Thread(this);
         this.mainThread.start();
      }

   }

   public void stop() {
      this.mainThread = null;
   }

   public void run() {
      for(; this.mainThread != null; this.repaint()) {
         try {
            Thread.sleep((long)this.threadSleep);
         } catch (InterruptedException var1) {
            return;
         }
      }

   }

   public void paint(Graphics var1) {
      this.offGrph.setColor(Color.black);
      this.offGrph.fillRect(0, 0, this.dim.width, this.dim.height);
      this.offGrph.drawImage(this.gAlc[0], 0, 0, this);
      this.drawBackSnow();
      var1.drawImage(this.offScrn, 0, 0, (ImageObserver)null);
   }

   public void update(Graphics var1) {
      this.paint(var1);
   }

   public void drawBackSnow() {
      this.offGrph.setColor(Color.white);

      for(int var1 = 0; var1 < this.snows; ++var1) {
         this.offGrph.fillRect(this.snowX[var1], this.snowY[var1], 1, 1);
         int[] var10000 = this.snowX;
         var10000[var1] += this.rand.nextInt() % 2 + this.wind;
         var10000 = this.snowY;
         var10000[var1] += (this.rand.nextInt() % 6 + 5) / 5 + 1;
         if (this.snowX[var1] >= this.dim.width) {
            this.snowX[var1] = 0;
         }

         if (this.snowX[var1] < 0) {
            this.snowX[var1] = this.dim.width - 1;
         }

         if (this.snowY[var1] >= this.dim.height || this.snowY[var1] < 0) {
            this.snowX[var1] = Math.abs(this.rand.nextInt() % this.dim.width);
            this.snowY[var1] = 0;
         }
      }

      switch(this.rand.nextInt() % 100) {
      case -2:
         this.wind = -2;
         return;
      case -1:
         this.wind = -1;
         return;
      case 0:
         this.wind = 0;
         return;
      case 1:
         this.wind = 1;
         return;
      case 2:
         this.wind = 2;
         return;
      default:
      }
   }
}
