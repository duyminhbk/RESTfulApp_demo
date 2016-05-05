package com.app.restfulapp.ultis;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Typeface;

import com.app.restfulapp.R;
import com.app.restfulapp.reports.FrgSLGDReport;
import com.app.restfulapp.reports.FrgSLKHReport;
import com.app.restfulapp.reports.FrgSLTTReport;
import com.app.restfulapp.reports.FrgSLTVReport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

@SuppressWarnings("ResourceType")
/*
	NOTE:
  |---------------------------------|
  |-TableA|-------TableB------------|
  |---------------------------------|
  |-------|-------------------------|
  |-------|-------------------------|
  |-------|-------------------------|
  |-TableC|-------TableD------------|
  |-------|-------------------------|
  |-------|-------------------------|
  |-------|-------------------------|
  |-------|-------------------------|
 */
public class ReportLayout extends RelativeLayout {

	public String reportType = "";

	private int columnWidth = 250;
	private final int WIDTH_DEFAULT = 250;
	private static final int DEFAUL_PADDING = 4;
	private static final int DEFAUL_BODY_PADDING = 4;
	public final String TAG = "TableMainLayout.java";

	private final String RptTypeCustomer = FrgSLKHReport.class.getSimpleName();
	private final String RptTypeSaleman = FrgSLTTReport.class.getSimpleName();
	private final String RptTypeChief = FrgSLTVReport.class.getSimpleName();
	private final String RptTypeDirector = FrgSLGDReport.class.getSimpleName();

	private int fixColumn = 1;

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
	private JSONObject mData;
	private int headerBg = Color.parseColor("#5B9BD5");
	private int headerTextColor = Color.parseColor("#FFFFFF");
	private int firstColBg = Color.parseColor("#BDD7EE");
	private int firstColTextColor = Color.parseColor("#000000");
	// L1 = P1, L2 = P2
	private int SumL2Bg = Color.parseColor("#0000DD");
	private int SumL2Text = Color.parseColor("#0000DD");
	private int SumL1Bg = firstColBg; // Color.parseColor("#FFFF00");
	private int lastRowBg = Color.parseColor("#af5B9BD5");
	private int lastRowTextColor = Color.parseColor("#FFFFFF");
	private int bodyBg = Color.parseColor("#afBDD7EE");
	private int bodyTextColor= Color.parseColor("#000000");

	private float[] CustomerReportColumnPercents = new float[]{0.29f, 0.11f, 0.10f, 0.14f, 0.35f};

	public ReportLayout(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public ReportLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttrs(attrs);
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
		this.setBackgroundColor(Color.parseColor("#dadada"));

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ReportLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		this(context, attrs, defStyleAttr);
	}

	public ReportLayout(Context context) {
		super(context, null, 0);
	}

	public void setColumnWidth(int[] widths){
		this.headerCellsWidth = widths;
	}

	public ReportLayout setColumnWidth(int index,int width){
		if(headerCellsWidth == null|| headerCellsWidth.length <= index) return this;
		headerCellsWidth[index] = width;
		return this;
	}

	public int[] getHeaderWidth() throws DataFormatException {
		if(mData == null){
			throw new DataFormatException("let init data first");
		}
		if( mData.isNull("index") ||mData.optJSONArray("index") == null ){
			throw new DataFormatException("bad format data");
		}

		if(mData.optJSONArray("index").length() == 0) return null;
		int [] result = new int[mData.optJSONArray("index").length()];
		int screenWidth =  Utility.getScreenSize((Activity) getContext()).x;
		int widthBaseOnScreen = screenWidth/result.length;
		// columnWidth is actually preferred fixed cols width
		int width = columnWidth;

		// Special code for Customer Report
		if(reportType.equals(RptTypeCustomer)) {

			if(result.length >= CustomerReportColumnPercents.length) {

				int total = 0;
				for (int i = 0; i < CustomerReportColumnPercents.length; i++) {
					if (i < CustomerReportColumnPercents.length - 1) {
						result[i] = (int) (screenWidth * CustomerReportColumnPercents[i]);
						total += result[i];
					} else {
						result[i] = screenWidth - total;
					}
				}

				return result;
			}
		}

		if(widthBaseOnScreen > columnWidth){
			width = widthBaseOnScreen;
		}
		//result[0] = width*2/3;
		result[0] = width;

		// Width of fixed columns except first column (result[0])
		int fixedColWidth = width;

		// Width other rightist cols
		int rightColWidth = fixedColWidth + 50;
		// if(rightColWidth >= 90)
		// 	rightColWidth = 90;

		// int widthOtherCol = width+(width-result[0])/(result.length-1);
		for(int i = 1; i<result.length; i++){
			// result[i] = widthOtherCol;
			if(i < fixColumn)
				result[i] = fixedColWidth;
			else
				result[i] = rightColWidth;
		}
		return result;
	}

	public void setData(JSONObject data){
		mData = data;
	}

	public void doLayout() throws DataFormatException {
		setDataAndLayout(mData);
		mData = null;
	}

	public ReportLayout makeUpHeader(int backgroundColor,int textColor){
		this.headerBg = backgroundColor;
		this.headerTextColor = textColor;
		return this;
	}

	public ReportLayout makeUpLeftColumn(int backgroundColor,int textColor){
		this.firstColBg = backgroundColor;
		this.firstColTextColor = textColor;
		return this;
	}

	public ReportLayout makeUpLastRow(int backgroundColor,int textColor){
		this.lastRowBg = backgroundColor;
		this.lastRowTextColor = textColor;
		return this;
	}

	public ReportLayout makeUpBody(int backgroundColor,int textColor){
		this.bodyBg = backgroundColor;
		this.bodyTextColor = textColor;
		return this;
	}

	// data struct
	//	{"index":["firstname","lastname","phone"],"data":{"1":["minh","pham","0123"],"2":["ha","nguyen",""],"3":["ngoc","","112"]}}
	public void setDataAndLayout(JSONObject data) throws DataFormatException {
		if(data == null){
			throw new DataFormatException("can not set null");
		}
		if( data.isNull("index") ||data.optJSONArray("index") == null ){
			throw new DataFormatException("missing index data");
		}
		mData = data;
		index = new String[data.optJSONArray("index").length()];

		JSONArray indexTemp = data.optJSONArray("index");
		if(indexTemp.length() == 0 || indexTemp.toString() == "[]"){
			throw new DataFormatException("index must not empty");
		}
		if(headerCellsWidth == null) {
			headerCellsWidth = getHeaderWidth();
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
				Integer ind = null;
				try{
					ind = Integer.parseInt(key);
//					Log.d("data index: ",ind.toString());
				}catch (Exception e){
					ind = null;
				}
				if(ind !=null && ind <= tableData.length){
					tableData[ind-1] = temp;
				}else {
					tableData[n] = temp;
				}
				n++;
			}
		}
		updateLayout();
	}

	private void updateLayout() {
		// add some table rows
		addDataToTableA();

		addDataToTableCAndTableD();

		addDataToTableB();

		resizeHeaderHeight();

		resizeBodyTableRowHeight();
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
		scrollViewC.setVerticalScrollBarEnabled(false);
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

	private void addDataToTableA(){
		TableRow componentATableRow = new TableRow(this.context);

		for(int i=0;i<fixColumn;i++){
			TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[i], LayoutParams.MATCH_PARENT);
			params.setMargins(2, 0, 0, 0);
			TextView textView = this.headerTextView(index[i]);
			textView.setBackgroundColor(headerBg);
			textView.setTextColor(headerTextColor);
			textView.setGravity(Gravity.CENTER);

			//if(reportType.equals(RptTypeCustomer)) {
			//	textView.setWidth(CustomerReportColumns[i]);
			//}
			//else {
				textView.setWidth(headerCellsWidth[i]);
			//}
			componentATableRow.addView(textView,params);
		}

		this.tableA.addView(componentATableRow);
	}

	private void addDataToTableB(){

		TableRow componentBTableRow = new TableRow(this.context);
		TableRow.LayoutParams params;
		for(int x=fixColumn; x<index.length; x++){
			if(headerCellsWidth == null) {
				params = new TableRow.LayoutParams(400, LayoutParams.MATCH_PARENT);
			}else{
				params = new TableRow.LayoutParams(headerCellsWidth[x], LayoutParams.MATCH_PARENT);
			}
			params.setMargins(2, 0, 0, 0);
			TextView textView = this.headerTextView(this.index[x]);
			textView.setTextColor(headerTextColor);
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundColor(headerBg);
			textView.setLayoutParams(params);
			//if(reportType.equals(RptTypeCustomer)) {
			//	textView.setWidth(CustomerReportColumns[x]);
			//}
			componentBTableRow.addView(textView);
		}
		this.tableB.addView(componentBTableRow);
	}

	// generate table row of table C and table D
	private void addDataToTableCAndTableD(){
		for(int i=0;i<tableData.length;i++){
			Object object = tableData[i];
			TableRow tableRowForTableC;
			TableRow tableRowForTableD;

			tableRowForTableC = this.tableRowForTableC((String[]) object, i == tableData.length - 1);
			tableRowForTableD = this.tableRowForTableD((String[]) object, i == tableData.length - 1);

			this.tableC.addView(tableRowForTableC);
			this.tableD.addView(tableRowForTableD);

		}
	}

	// a TableRow for table C
	TableRow tableRowForTableC(String[] data, boolean isLast){

		TableRow tableRowForTableC = new TableRow(this.context);
		for(int i =0;i<fixColumn;i++){
			int width = viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(i));
			TableRow.LayoutParams params = new TableRow.LayoutParams( width, LayoutParams.MATCH_PARENT);
			params.setMargins(0, 2, 2, 0);

			TextView textView = this.bodyTextView(data[i]);
			if(isLast){
				textView.setTextColor(lastRowTextColor);
				textView.setBackgroundColor(lastRowBg);
				textView.setText(Html.fromHtml("<b>" + data[i] + "</b>"));
			} else {
				textView.setTextColor(firstColTextColor);
				textView.setBackgroundColor(firstColBg);
			}
			tableRowForTableC.addView(textView, params);
		}

		return tableRowForTableC;
	}

	TableRow tableRowForTableD(String[] data, boolean isLast){

		TableRow taleRowForTableD = new TableRow(this.context);
		TableRow.LayoutParams params;

		int rowBg = bodyBg;
		int rowTextColor = bodyTextColor;
		boolean L2 = false;

		// Normal lines
		if(reportType.equals(RptTypeCustomer)) {
			if("Total of P2".equals(data[0])) {
				// rowBg = SumL2Bg;
				L2 = true;
			}
			else if("Total of P1".equals(data[0])) {
				rowBg = SumL1Bg;
			}
		}
		else if(reportType.equals(RptTypeSaleman)) {
			if(data[1] != null && data[1].endsWith(" Total")) {
				// rowBg = SumL2Bg;
				L2 = true;
			}
			else if(data[0] != null && data[0].endsWith(" Total")) {
				rowBg = SumL1Bg;
			}
		}
		else if(reportType.equals(RptTypeChief)) {
			if("Total".equals(data[1])) {
				rowBg = SumL1Bg;
			}
		}
		else if(reportType.equals(RptTypeDirector)) {
			if(data[0] != null && !data[0].equals("")) {
				rowBg = SumL1Bg;
			}
		}

		for(int x=fixColumn ; x<index.length; x++){
			if(headerCellsWidth == null){
				params = new TableRow.LayoutParams(400, LayoutParams.MATCH_PARENT);
			}else{
				params = new TableRow.LayoutParams(headerCellsWidth[x], LayoutParams.MATCH_PARENT);
			}
			params.setMargins(2, 2, 0, 0);
			TextView textViewB = this.bodyTextView(data[x]);

			if(isLast){
				textViewB.setTextColor(lastRowTextColor);
				textViewB.setBackgroundColor(lastRowBg);
				textViewB.setText(Html.fromHtml("<b>" + data[x] + "</b>"));
			} else {
				//textViewB.setTextColor(bodyTextColor);
				//textViewB.setBackgroundColor(bodyBg);
				textViewB.setTextColor(rowTextColor);
				textViewB.setBackgroundColor(rowBg);
				if(L2) {
					textViewB.setTypeface(null, Typeface.BOLD);
				}
			}
			textViewB.setPadding(DEFAUL_PADDING, DEFAUL_PADDING, 2, DEFAUL_PADDING);
			textViewB.setGravity(Gravity.RIGHT);
			taleRowForTableD.addView(textViewB,params);
		}

		return taleRowForTableD;

	}

	// table cell standard TextView
	TextView bodyTextView(String label){

		TextView bodyTextView = new TextView(this.context);
		bodyTextView.setBackgroundColor(Color.WHITE);
		bodyTextView.setText(Html.fromHtml(label));
		bodyTextView.setGravity(Gravity.CENTER);
		bodyTextView.setPadding(DEFAUL_BODY_PADDING, DEFAUL_BODY_PADDING, DEFAUL_BODY_PADDING, DEFAUL_BODY_PADDING);

		return bodyTextView;
	}

	// header standard TextView
	TextView headerTextView(String label){

		TextView headerTextView = new TextView(this.context);
		headerTextView.setBackgroundColor(Color.WHITE);
		headerTextView.setText(Html.fromHtml("<b>" + label + "</b>"));
		headerTextView.setGravity(Gravity.CENTER);
		headerTextView.setPadding(DEFAUL_PADDING, DEFAUL_PADDING, DEFAUL_PADDING, DEFAUL_PADDING);

		return headerTextView;
	}

	// resizing TableRow height starts here
	void resizeHeaderHeight() {

		TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
		TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

		int rowAHeight = this.viewHeight(productNameHeaderTableRow);
		int rowBHeight = this.viewHeight(productInfoTableRow);

		int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

		this.matchLayoutHeight(productNameHeaderTableRow, finalHeight >= 100 ? finalHeight : 100);
		this.matchLayoutHeight(productInfoTableRow, finalHeight >= 100 ? finalHeight : 100);
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
		return view.getMeasuredWidth() ;
	}

	public void reLayout() {
		clearLayout();
		try {
			setDataAndLayout(mData);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void clearLayout() {
		headerCellsWidth = null;
		removeTableChild(tableA, tableD, tableB, tableC);
	}

	private void removeTableChild(TableLayout... tables) {
		if(tables == null || tables.length ==0) return;
		for (TableLayout table: tables) {
			table.removeAllViewsInLayout();
		}
	}

	public void getAttrs(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray attrsArray =
					getContext().obtainStyledAttributes(attrs, R.styleable.reportLayout);

			fixColumn = attrsArray.getInteger(R.styleable.reportLayout_fixedColumn, 1);
			columnWidth = attrsArray.getDimensionPixelSize(R.styleable.reportLayout_widthColumn, WIDTH_DEFAULT);

			attrsArray.recycle();
		}
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
