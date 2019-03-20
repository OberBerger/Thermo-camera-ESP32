package com.example.thermocamera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class CanvasView extends View{
	
	private int camColors[] = {10, 20, 37, 46, 50, 58, 66, 70, 79, 65621, 
	 65623, 131164, 262241, 262243, 393319, 524395, 655472, 721011, 852085, 917623, 
	 1048696, 1245307, 1507453, 1638526, 1835137, 2097284, 2228357, 2490503, 2752649, 3014795, 
	 3145868, 3408014, 3670159, 3735696, 3932306, 4128915, 4259988, 4456597, 4653206, 4784278, 
	 4980887, 5177495, 5308567, 5505176, 5767321, 6029465, 6095002, 6357147, 6553755, 6684827, 
	 6946971, 7143580, 7274652, 7405725, 7667869, 7798941, 7995549, 8257693, 8454301, 8585373, 
	 8781981, 8978589, 9044125, 9240733, 9502876, 9633948, 9830555, 10027163, 10158235, 10289307, 
	 10485915, 10616987, 10748059, 10944666, 11075737, 11141273, 11337881, 11469208, 11534744, 11600279, 
	 11731350, 11797142, 11928213, 12059541, 12125333, 12190868, 12322195, 12453522, 12519058, 12584849, 
	 12650640, 12650896, 12782222, 12848269, 12913804, 12979850, 13111432, 13177223, 13243269, 13309060, 
	 13440641, 13506432, 13572220, 13638265, 13704056, 13769845, 13835890, 13836401, 13902446, 13968489, 
	 14034279, 14165860, 14232160, 14297950, 14364250, 14430036, 14496078, 14561866, 14627908, 14628413, 
	 14694202, 14694707, 14826285, 14826538, 14892579, 14958877, 14959132, 15025177, 15091222, 15157268, 
	 15157523, 15223824, 15289614, 15289613, 15355660, 15421706, 15421962, 15488009, 15488776, 15489032, 
	 15555079, 15621126, 15621125, 15621637, 15687684, 15688196, 15753987, 15754499, 15820291, 15820547, 
	 15821058, 15821570, 15821570, 15887617, 15953665, 15953921, 15954433, 16020480, 16020992, 16021248, 
	 16022016, 16088064, 16088576, 16154624, 16155136, 16220928, 16221440, 16287488, 16287744, 16288000, 
	 16288512, 16288768, 16354560, 16355072, 16355584, 16355840, 16421888, 16422400, 16488448, 16488960, 
	 16555264, 16555776, 16556288, 16622336, 16622592, 16623360, 16624128, 16624640, 16624896, 16690944, 
	 16691456, 16691712, 16692224, 16692736, 16693248, 16693504, 16694016, 16694272, 16694784, 16695552, 
	 16696064, 16696320, 16696832, 16697344, 16697601, 16697857, 16698370, 16698626, 16699140, 16699397,
	 16699654, 16700425, 16700938, 16701195, 16701709, 16767502, 16768018, 16768020, 16768537, 16768798, 
	 16769056, 16769572, 16769832, 16770091, 16770353, 16770616, 16770876, 16771395, 16771913, 16772176, 
	 16772436, 16772699, 16772963, 16772967, 16773230, 16773495, 16773499, 16773765, 16774030, 16774290, 
	 16774298, 16774562, 16774566, 16774831, 16775094, 16775357, 16775361, 16775623, 16775629, 16775889, 
	 16776152, 16776415, 16776674, 16776680, 16776942, 16776945};
	
	private int camColors2[] = {5178239, 4653951, 4653951, 4653951, 4653959, 4129671, 4129671, 4129671, 4129671, 3605383, 
			3605383, 3605383, 3081095, 3081095, 3081095, 3081095, 2556807, 2556807, 2556807, 2032519, 
			2032519, 2032527, 2032527, 1508239, 1508239, 1508239, 983951, 983951, 983951, 459663, 
			459663, 459663, 459663, 459663, 460687, 460687, 461711, 462743, 462743, 463767, 
			464791, 464791, 465815, 466839, 466839, 467863, 468887, 469911, 469911, 470935, 
			471959, 471959, 472983, 474007, 475039, 475039, 476063, 477087, 478111, 478111, 
			479135, 480159, 481183, 482207, 482207, 483231, 484255, 485279, 485279, 486303, 
			487327, 488359, 489383, 490407, 490407, 491431, 492455, 493479, 494503, 495527, 
			495527, 496551, 497575, 498599, 499623, 500647, 501671, 501671, 502695, 502695, 
			503719, 503719, 503711, 503711, 503711, 503703, 503703, 503703, 503695, 504719, 
			504719, 504711, 504711, 504703, 504703, 504703, 504695, 505719, 505719, 505711, 
			505711, 505711, 505703, 505703, 505695, 506719, 506719, 506711, 506711, 506711, 
			506703, 506703, 506695, 507719, 507719, 507711, 507711, 507703, 507703, 507703, 
			507695, 507695, 508711, 508711, 508711, 508703, 508703, 508695, 508695, 508687, 
			509711, 509711, 509703, 509703, 509703, 509703, 1033991, 1033991, 1035015, 1559303, 
			1559303, 2083591, 2083591, 2607879, 2607879, 3132167, 3133191, 3657479, 3657479, 4181767, 
			4181767, 4181767, 4706055, 4706055, 5230343, 5231367, 5755655, 5755655, 6279943, 6279943, 
			6804231, 6804231, 7328519, 7329543, 7853831, 7853831, 8378119, 8378119, 8902407, 8902407, 
			9426695, 9427719, 9952007, 9952007, 10476295, 10476295, 11000583, 11524871, 11524871, 12050183, 
			12050183, 12574471, 12574471, 13098759, 13098759, 13623047, 13623047, 14147335, 14148359, 14672647, 
			14671623, 14670599, 14669575, 14668551, 14668551, 15191815, 15190791, 15189767, 15188743, 15187719, 
			15186695, 15185671, 15184647, 15183623, 15182599, 15181575, 15180551, 15179527, 15178503, 15177479, 
			15176455, 15175431, 15698695, 15697671, 15696647, 15695623, 15694599, 15693575, 15692551, 15691527, 
			15690503, 15689479, 15688455, 15687431, 15686407, 15685383, 15684359, 15683335, 15682311, 16205575, 
			16204551, 16203527, 16202503, 16201479, 16200455, 16199431, 16198407, 16196359, 16195335, 16194311, 
			16193287, 16192263, 16191239, 16190215, 16189191, 16712455};
	

	public int width=32	; //(45) ITT LEHET MÓDOSÍTANI A KÉP FELBONTÁSÁT
	public int height=width ; 
	private Path mPath;
	Context context;
	private Paint mPaint;
	private boolean st=true;
	private boolean colst=true;

	public CanvasView(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;

		// we set a new Path
		mPath = new Path();

		// and we set a new Paint with the desired attributes
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeWidth(2f);
		


	}


	// override onDraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int w = canvas.getWidth();
		int h = canvas.getHeight();
		int min = w;
		if (h<w) min=h;
		byte[] pix_B= MainActivity.get_pixs();

		//Log.d("onDraw", "width="+String.valueOf(width));
		int BoxPix=(int)(min/width);
		int BoxSize=width*BoxPix;
		int z=((w-BoxSize)/2);
		
		int[] pix_I =  new int[width * height];
		
		for (int i=0; i<64; i++) {pix_I[i]=pix_B[i] & 0xFF;}	
		int[] des = filter(pix_I, 8, 8);
		if (colst) {for (int i=0; i<width * height; i++) {des[i]=camColors[(int) ((int)(des[i] & 0xFF))] | 0xFF000000;}}
		else for (int i=0; i<width * height; i++) {des[i]=(camColors2[des[i] & 0xFF]) | 0xFF000000;}
		
				
		 for (int y=0; y<height; y++) {
			    for (int x=0; x<width; x++) {
			      mPaint.setColor((int) des[y*width+x]);
			      mPaint.setStyle(Paint.Style.FILL);
			      canvas.drawRect(((x)*(BoxPix))+z, y*(BoxPix), ((x+1)*(BoxPix))+z, (y+1)*(BoxPix), this.mPaint);
			    } 
		 }
		 
		 if (st) {
			 mPaint.setColor(Color.WHITE);
			 if (colst && (pix_I[35]>180)) mPaint.setColor(Color.DKGRAY);
			 mPaint.setStrokeWidth(w/200*1);
			 canvas.drawLine(BoxSize/2+z, BoxSize/2-10, BoxSize/2+z, BoxSize/2+10, mPaint);
			 canvas.drawLine(BoxSize/2-10+z, BoxSize/2, BoxSize/2+10+z, BoxSize/2, mPaint);
			 mPaint.setColor(Color.WHITE);
			 if (colst && (pix_I[35]>180)) mPaint.setColor(Color.DKGRAY);
			 mPaint.setTextSize((float) (BoxSize*0.1)); 
			 canvas.drawText(String.valueOf(((pix_B[66])-30))+" °C", (float) (BoxSize/2.7)+z, (float) (BoxSize/1.5), mPaint);
			 for (int i=0; i<BoxSize; i++) {
				 if (colst) {mPaint.setColor(camColors[(int) ((float)i*((float) 255/(BoxSize)))] + 0xFF000000);}
				 else {mPaint.setColor((camColors2[(int) ((float)i*((float) 255/(BoxSize)))]) + 0xFF000000);}
				 canvas.drawLine(i + z, BoxSize , i + z, BoxSize-BoxPix , mPaint);
			 }
		 }	
	}


	public void clearCanvas() {
		mPath.reset();
		invalidate();
	}


	//override the onTouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		float x = event.getX();
		float y = event.getY();
		int w = getWidth();
		int h = getHeight();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			if ((x>w/3*2) && (width<64)) {
				width++; height=width;
				}
			if ((x<w/3)  && (width>8)) {
				width--; height=width;
				}
			if (x>w/3 && x<w/3*2  && y>h/2) st=!st;			
			if (x>w/3 && x<w/3*2  && y<h/2) colst=!colst;
			break;
		}
		return true;
	}
	
    public int[] filter( int[] src, int w, int h ) {
        int[] dst = new int[width * height];
        
        //Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);
        Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Bitmap.Config.RGB_565);
        //Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Bitmap.Config.ALPHA_8);
        Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, width, height, true);
        dstBitmap.getPixels(dst, 0, width, 0, 0, width, height);
        
        srcBitmap.recycle();
        dstBitmap.recycle();

        return dst;
    }
    
    public int rgbWordToRGB(int rgb565){

    	 final int RGB565_MASK_RED       = 0xF800;
    	 final int RGB565_MASK_GREEN     = 0x07E0;  
    	 final int RGB565_MASK_BLUE      = 0x001F; 
    	 
    	 int[] rgb24 = new int[3];
    	 rgb24[2] = (rgb565 & RGB565_MASK_RED) >> 11;     
    	 rgb24[1] = (rgb565 & RGB565_MASK_GREEN) >> 5;  
    	 rgb24[0] = (rgb565 & RGB565_MASK_BLUE);  

    	 //amplify the image  
    	 rgb24[2] <<= 3;  
    	 rgb24[1] <<= 2;  
    	 rgb24[0] <<= 3; 
    	 
    	 rgb24[2] = rgb24[2] | 7;  
    	 rgb24[1] = rgb24[1] | 3;  
    	 rgb24[0] = rgb24[0] | 7; 
    	 
    	 return (rgb24[2] << 16) +(rgb24[1] << 8)+rgb24[0];
    	}
    
    public int RGB565(int rgb) {
    	int[] RGB565 = new int[2];
        int or = ((rgb & 0x00FF0000) >> 19);
        int og = ((rgb & 0x0000FF00) >> 10);
        int ob = ((rgb & 0x000000FF) >> 3);	
		RGB565[0] = (ob | (og << 5)) & 0xFF;
		RGB565[1] = ((og >> 3) | (or << 3) & 0xFF) << 8;
        return RGB565[1] | RGB565[0];
    }
    
	
}


