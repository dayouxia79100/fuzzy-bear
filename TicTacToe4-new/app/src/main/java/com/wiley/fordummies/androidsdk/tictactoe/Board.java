package com.wiley.fordummies.androidsdk.tictactoe;

import com.wiley.fordummies.androidsdk.tictactoe.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Board extends View {

	private final GameSession gameSession;	// game context (parent) 
	private float width; 		// width of one block
	private float height;   	// will be same as width;
	private final float strokeWidth = 2;
	private final float lineWidth = 10;
	private GameGrid grid=null;
	private boolean enabled = true;
	
	static Bitmap symX=null, symO=null, symBlank=null; // rr
	static boolean bitMapsInitialized=false;
	
	public Board(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.gameSession = (GameSession) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	public void setGrid(GameGrid aGrid){
		grid = aGrid;
	}
	protected void onSizeChanged  (int w, int h, int oldw, int oldh)  
	{
		width = w / 3f;
		height = h / 3f;
		if (w < h) height = width;
		else width = height;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float right = getWidth();
		float bottom = getHeight();
		
		if(right < bottom) bottom = right; else right = bottom;
		
		Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(getResources().getColor(R.color.white));
		canvas.drawRect(0, 0, right, bottom, backgroundPaint);
		
		Paint darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		darkPaint.setColor(getResources().getColor(R.color.dark));
		darkPaint.setStrokeWidth(strokeWidth);
		
		Paint lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		lightPaint.setColor(getResources().getColor(R.color.light));
		lightPaint.setStrokeWidth(strokeWidth);
		
		Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(getResources().getColor(R.color.dark));
		linePaint.setStrokeWidth(lineWidth);
		linePaint.setStrokeCap(Cap.ROUND);
		
		// Drawing lines
		for(int i=0; i<GameGrid.SIZE-1; i++)
		{
			canvas.drawLine( 0, ( i + 1 ) * height , right, ( i + 1 ) * height, darkPaint );
			canvas.drawLine( 0, ( i + 1 ) * height + 1 , right, ( i + 1 ) * height + 1, lightPaint );
			canvas.drawLine( ( i + 1 ) * height , 0, ( i + 1 ) * height, bottom, darkPaint );
			canvas.drawLine( ( i + 1 ) * height + 1  , 0, ( i + 1 ) * height + 1, bottom, lightPaint );
		}

		Paint ditherPaint = new Paint();
		ditherPaint.setDither(true);
		
		float offsetX = 0;
		float offsetY = 0;
		for(int i = 0; i < GameGrid.SIZE; i++){
			for(int j = 0; j < GameGrid.SIZE; j++){
				Bitmap symSelected = getBitmapForSymbol(grid.getValueAtLocation(i, j));
				offsetX = (int) ( ( (width  - symSelected.getWidth())  / 2) + (i * width) ) ;
				offsetY = (int) ( ( (height - symSelected.getHeight()) / 2) + (j * height) ) ;
				canvas.drawBitmap(symSelected, offsetX, offsetY, ditherPaint);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		if( !this.enabled ) {
			System.out.println("Board.onTouchEvent: Board not enabled");
			return false;
		}
		
		int posX = 0;
		int posY = 0;	
		int action = event.getAction();		
		
		switch (action){
			case MotionEvent.ACTION_DOWN:
				float x = event.getX();
				float y = event.getY();
				System.out.println("coordinates: " + x + "," + y);
				if( x > width && x < width * 2 ) posX = 1;
				if( x > width * 2 && x < width * 3 ) posX = 2;
				
				if( y > height && y < height * 2 ) posY = 1;
				if( y > height * 2 && y < height * 3 ) posY = 2;

				gameSession.humanTakesATurn(posX, posY);				
				break;
		}
		return super.onTouchEvent(event);
	}

	protected boolean placeSymbol(int x, int y){
	    System.out.println("Thread ID in Board.placeSymbol:" + Thread.currentThread().getId());
		invalidateBlock(x, y);
		return true;
	}
		
	public void invalidateBlock(int x, int y) {
		Rect selBlock = new Rect( (int)( x * width ), (int)( y * height), (int)( (x+1) * width ), (int)( (y+1) * height));
		invalidate( selBlock );
	}
	
	public boolean isInputEnabled(){
		return this.enabled;
	}
	protected void disableInput(){
		this.enabled = false;
		System.out.println("Board.disableInput: Board not enabled");
	}
	
	protected void enableInput()
	{
		this.enabled = true;
		System.out.println("Board.enableInput: Board enabled");
	}
	public Bitmap getBitmapForSymbol(Symbol aSymbol){

		if (!bitMapsInitialized){
		    Resources res = getResources();
		    symX = BitmapFactory.decodeResource(res, R.drawable.x);
		    symO = BitmapFactory.decodeResource(res, R.drawable.o);
		    symBlank = BitmapFactory.decodeResource(res, R.drawable.blank);
		    bitMapsInitialized=true;
        }


		Bitmap symSelected = symBlank;
		
		if (aSymbol == Symbol.SymbolXCreate()) 
			symSelected = symX;
		else if (aSymbol == Symbol.SymbolOCreate()) 
			symSelected = symO;
		return symSelected;
	}
}

