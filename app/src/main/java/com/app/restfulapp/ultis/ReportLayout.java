package com.app.restfulapp.ultis;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.restfulapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ResourceType")
public class ReportLayout extends RelativeLayout {

	public final String TAG = "TableMainLayout.java";

	// set the header titles
//	String headers[]= {
//			"Header 1 \n multi-lines",
//			"Header 2",
//			"Header 3",
//			"Header 4",
//			"Header 5",
//			"Header 6",
//			"Header 7",
//			"Header 8"
//	} ;

	TableLayout tableA;
	TableLayout tableB;
	TableLayout tableC;
	TableLayout tableD;

	HorizontalScrollView horizontalScrollViewB;
	HorizontalScrollView horizontalScrollViewD;

	ScrollView scrollViewC;
	ScrollView scrollViewD;

	Context context;


	int headerCellsWidth[] ;
	private String[] index;
	private Object[] tableData;

	public ReportLayout(Context context, AttributeSet attrs) {
		this(context);
	}

	public ReportLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ReportLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		this(context);
	}
	public ReportLayout(Context context) {

		super(context);
		this.context = context;

		// initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
		this.initComponents();
		this.setComponentsId();
		this.setScrollViewAndHorizontalScrollViewTag();


		// no need to assemble component A, since it is just a table
		this.horizontalScrollViewB.addView(this.tableB);

		this.scrollViewC.addView(this.tableC);

		this.scrollViewD.addView(this.horizontalScrollViewD);
		this.horizontalScrollViewD.addView(this.tableD);

		// add the components to be part of the main layout
		this.addComponentToMainLayout();
		this.setBackgroundColor(Color.RED);
	}

	// data struct
	//	{"index":["firstname","lastname","phone"],"data":{"1":["minh","pham","0123"],"2":["ha","nguyen",""],"3":["ngoc","","112"]}}
	public void setData(JSONObject data) throws DataFormatException {
		if(data == null){
			throw new DataFormatException("can not set null");
		}
		if( data.isNull("index")){
			throw new DataFormatException("missing index data");
		}
		index = new String[data.optJSONArray("index").length()];
		JSONArray indexTemp = data.optJSONArray("index");
		if(indexTemp.length() == 0 || indexTemp.toString() == "[]"){
			throw new DataFormatException("index must not empty");
		}
		for(int i = 0; i<indexTemp.length();i++){
			index[i] = indexTemp.optString(i);
		}
		if(!data.isNull("data")){
			JSONObject dataTemp = data.optJSONObject("data");
			tableData = new Object[dataTemp.length()];
			Iterator<String> keys = dataTemp.keys();
			int n =0;
			while (keys.hasNext()){
				String key = keys.next();
				JSONArray arr = dataTemp.optJSONArray(key);
				String[] temp = new String[arr.length()];
				for(int i = 0; i<arr.length();i++){
					temp[i] = arr.optString(i);
				}
				tableData[n] = temp;
				n++;
			}
		}
		updateLayout();
	}

	private void updateLayout() {
		// add some table rows
		addTableRowToTableA();
		addTableRowToTableB();

		resizeHeaderHeight();

		getTableRowHeaderCellWidth();

		generateTableC_AndTable_B();

		resizeBodyTableRowHeight();
	}

	List<SampleObject> sampleObjects(){

		List<SampleObject> sampleObjects = new ArrayList<SampleObject>();

		for(int x=1; x<=50; x++){

			SampleObject sampleObject = new SampleObject(
				x%4 != 0? "": (x+" Phan tuan trung ") ,
				"6080\n"+x+" Phan Tuan Trung"+x+ "\n0938084093"+x,
				"55,500" + x,
				"1,027,150"+ x,
				"877,035" + x,
				"150,115" + x,
				"1" + x+"%"
			);

			sampleObjects.add(sampleObject);
		}
		SampleObject sampleObject = new SampleObject(
				"","TOTAL","900,220","15,080,245","15,186,716","",""
		);
		sampleObjects.add(sampleObject);
		return sampleObjects;

	}

	// initalized components
	private void initComponents(){

		this.tableA = new TableLayout(this.context);
		this.tableB = new TableLayout(this.context);
		this.tableC = new TableLayout(this.context);
		this.tableD = new TableLayout(this.context);

		this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
		this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

		this.scrollViewC = new MyScrollView(this.context);
		this.scrollViewD = new MyScrollView(this.context);

		this.tableA.setBackgroundColor(Color.GREEN);
		this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

	}

	// set essential component IDs
	private void setComponentsId(){
		this.tableA.setId(1);
		this.horizontalScrollViewB.setId(2);
		this.scrollViewC.setId(3);
		this.scrollViewD.setId(4);
	}

	// set tags for some horizontal and vertical scroll view
	private void setScrollViewAndHorizontalScrollViewTag(){

		this.horizontalScrollViewB.setTag("horizontal scroll view b");
		this.horizontalScrollViewD.setTag("horizontal scroll view d");

		this.scrollViewC.setTag("scroll view c");
		this.scrollViewD.setTag("scroll view d");
	}

	// we add the components here in our TableMainLayout
	private void addComponentToMainLayout(){

		// RelativeLayout params were very useful here
		// the addRule method is the key to arrange the components properly
		LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

		LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

		LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
		componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

		// 'this' is a relative layout,
		// we extend this table layout as relative layout as seen during the creation of this class
		this.addView(this.tableA);
		this.addView(this.horizontalScrollViewB, componentB_Params);
		this.addView(this.scrollViewC, componentC_Params);
		this.addView(this.scrollViewD, componentD_Params);

	}



	private void addTableRowToTableA(){
		this.tableA.addView(this.componentATableRow());
	}

	private void addTableRowToTableB(){
		this.tableB.addView(this.componentBTableRow());
	}

	// generate table row of table A
	TableRow componentATableRow(){

		TableRow componentATableRow = new TableRow(this.context);
		TextView textView = this.headerTextView(index[0]);
		componentATableRow.addView(textView);

		return componentATableRow;
	}

	// generate table row of table B
	TableRow componentBTableRow(){

		TableRow componentBTableRow = new TableRow(this.context);
		int headerFieldCount = index.length;

		TableRow.LayoutParams params = new TableRow.LayoutParams(400, LayoutParams.MATCH_PARENT);
		params.setMargins(2, 0, 0, 0);

		for(int x=0; x<(headerFieldCount-1); x++){
			TextView textView = this.headerTextView(this.index[x+1]);
			textView.setLayoutParams(params);
			componentBTableRow.addView(textView);
		}

		return componentBTableRow;
	}

	// generate table row of table C and table D
	private void generateTableC_AndTable_B(){

		// just seeing some header cell width
		for(int x=0; x<this.headerCellsWidth.length; x++){
			Log.v("TableMainLayout.java", this.headerCellsWidth[x] + "");
		}

		for(Object object : tableData){
			TableRow tableRowForTableC = this.tableRowForTableC((String[])object);
			TableRow taleRowForTableD = this.taleRowForTableD((String[])object);

			tableRowForTableC.setBackgroundColor(Color.LTGRAY);
			taleRowForTableD.setBackgroundColor(Color.LTGRAY);

			this.tableC.addView(tableRowForTableC);
			this.tableD.addView(taleRowForTableD);

		}
	}

	// a TableRow for table C
	TableRow tableRowForTableC(String[] data){

		TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
		params.setMargins(0, 2, 0, 0);

		TableRow tableRowForTableC = new TableRow(this.context);
		TextView textView = this.bodyTextView(data[0]);
		tableRowForTableC.addView(textView,params);

		return tableRowForTableC;
	}

	TableRow taleRowForTableD(String[] data){

		TableRow taleRowForTableD = new TableRow(this.context);

		int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();


		for(int x=1 ; x<loopCount; x++){
			TableRow.LayoutParams params = new TableRow.LayoutParams( 400, LayoutParams.MATCH_PARENT);
			params.setMargins(2, 2, 0, 0);

			TextView textViewB = this.bodyTextView(data[x]);
			taleRowForTableD.addView(textViewB,params);
		}

		return taleRowForTableD;

	}

	// table cell standard TextView
	TextView bodyTextView(String label){

		TextView bodyTextView = new TextView(this.context);
		bodyTextView.setBackgroundColor(Color.WHITE);
		bodyTextView.setText(label);
		bodyTextView.setGravity(Gravity.CENTER);
		bodyTextView.setPadding(5, 5, 5, 5);

		return bodyTextView;
	}

	// header standard TextView
	TextView headerTextView(String label){

		TextView headerTextView = new TextView(this.context);
		headerTextView.setBackgroundColor(Color.WHITE);
		headerTextView.setText(label);
		headerTextView.setGravity(Gravity.CENTER);
		headerTextView.setPadding(5, 5, 5, 5);

		return headerTextView;
	}

	// resizing TableRow height starts here
	void resizeHeaderHeight() {

		TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
		TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

		int rowAHeight = this.viewHeight(productNameHeaderTableRow);
		int rowBHeight = this.viewHeight(productInfoTableRow);

		TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
		int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

		this.matchLayoutHeight(tableRow, finalHeight);
	}

	void getTableRowHeaderCellWidth(){

		int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
		int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();;

		for(int x=0; x<(tableAChildCount+tableBChildCount); x++){

			if(x==0){
				this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
			}else{
				this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-1));
			}

		}
	}

	// resize body table row height
	void resizeBodyTableRowHeight(){

		int tableC_ChildCount = this.tableC.getChildCount();

		for(int x=0; x<tableC_ChildCount; x++){

			TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
			TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

			int rowAHeight = this.viewHeight(productNameHeaderTableRow);
			int rowBHeight = this.viewHeight(productInfoTableRow);

			TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
			int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

			this.matchLayoutHeight(tableRow, finalHeight);
		}

	}

	// match all height in a table row
	// to make a standard TableRow height
	private void matchLayoutHeight(TableRow tableRow, int height) {

		int tableRowChildCount = tableRow.getChildCount();

		// if a TableRow has only 1 child
		if(tableRow.getChildCount()==1){

			View view = tableRow.getChildAt(0);
			TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
			params.height = height - (params.bottomMargin + params.topMargin);

			return ;
		}

		// if a TableRow has more than 1 child
		for (int x = 0; x < tableRowChildCount; x++) {

			View view = tableRow.getChildAt(x);

			TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

			if (!isTheHeighestLayout(tableRow, x)) {
				params.height = height - (params.bottomMargin + params.topMargin);
				return;
			}
		}

	}

	// check if the view has the highest height in a TableRow
	private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

		int tableRowChildCount = tableRow.getChildCount();
		int heighestViewPosition = -1;
		int viewHeight = 0;

		for (int x = 0; x < tableRowChildCount; x++) {
			View view = tableRow.getChildAt(x);
			int height = this.viewHeight(view);

			if (viewHeight < height) {
				heighestViewPosition = x;
				viewHeight = height;
			}
		}

		return heighestViewPosition == layoutPosition;
	}

	// read a view's height
	private int viewHeight(View view) {
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		return view.getMeasuredHeight();
	}

	// read a view's width
	private int viewWidth(View view) {
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		return view.getMeasuredWidth();
	}

	// horizontal scroll view custom class
	class MyHorizontalScrollView extends HorizontalScrollView {

		public MyHorizontalScrollView(Context context) {
			super(context);
		}
		
		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			String tag = (String) this.getTag();
			
			if(tag.equalsIgnoreCase("horizontal scroll view b")){
				horizontalScrollViewD.scrollTo(l, 0);
			}else{
				horizontalScrollViewB.scrollTo(l, 0);
			}
		}
		
	}

	// scroll view custom class
	class MyScrollView extends ScrollView {

		public MyScrollView(Context context) {
			super(context);
		}
		
		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			
			String tag = (String) this.getTag();
			
			if(tag.equalsIgnoreCase("scroll view c")){
				scrollViewD.scrollTo(0, t);
			}else{
				scrollViewC.scrollTo(0,t);
			}
		}
	}

	public static class DataFormatException extends Exception{

		public DataFormatException(String s) {
			super(s);
		}
	}

	
}
