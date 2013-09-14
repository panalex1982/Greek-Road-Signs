package com.bue.signindex;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends TabActivity {
	
	final int WARNINIG_SIGN=0;
	final int PROHIBITION_SIGN=1;
	final int DIRECTION_SIGN=2;
	final int INFORMATION_SIGN=3;
	final int ADDITIONAL_SIGN=4;
	ArrayList<Sign> mSigns;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			//Load Signs
			mSigns=loadSigns();
			if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
				signsLinearLayoutCreate(6);
			}else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
				signsLinearLayoutCreate(4);
			}else{
				signsLinearLayoutCreate(5);
			}
			
		} catch (Exception ex) {
			ex.getMessage();
		}
	}
	
	private void signsLinearLayoutCreate(int numberOfSignsPerLine){
		setContentView(R.layout.activity_main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();

		TabHost.TabSpec spec;	

		// First Tab
		LinearLayout warniningLayout = (LinearLayout) findViewById(R.id.WarningSignsLinearLayout);
		warniningLayout.addView(addSigns(WARNINIG_SIGN,numberOfSignsPerLine));
		spec = tabHost
				.newTabSpec("Warning Signs")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_warning_signs))
				.setContent(R.id.tab1);
		tabHost.addTab(spec);
		// Second Tab
		LinearLayout prohibitionLayout = (LinearLayout) findViewById(R.id.ProhibitionSignsLinearLayout);
		prohibitionLayout.addView(addSigns(PROHIBITION_SIGN,numberOfSignsPerLine));
		spec = tabHost
				.newTabSpec("Prohibition Signs")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_prohibition_signs))
				.setContent(R.id.tab2);		
		tabHost.addTab(spec);
		//Third Tab
		LinearLayout directionLayout = (LinearLayout) findViewById(R.id.InformationSignsLinearLayout);
		directionLayout.addView(addSigns(DIRECTION_SIGN,numberOfSignsPerLine));
		spec = tabHost
				.newTabSpec("Direction Signs")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_traffic_priority))
				.setContent(R.id.tab3);		
		tabHost.addTab(spec);
		//Fourth Tab
		LinearLayout informationLayout = (LinearLayout) findViewById(R.id.DirectionSignsLinearLayout);
		informationLayout.addView(addSigns(INFORMATION_SIGN,numberOfSignsPerLine));
		spec = tabHost
				.newTabSpec("Information Signs")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_information_signs))
				.setContent(R.id.tab4);		
		tabHost.addTab(spec);
		//Fifth Tab
		LinearLayout additionalLayout = (LinearLayout) findViewById(R.id.AdditionalSignsLinearLayout);
		additionalLayout.addView(addSigns(ADDITIONAL_SIGN,numberOfSignsPerLine));
		spec = tabHost
				.newTabSpec("Additiona Signs")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_additional_signs))
				.setContent(R.id.tab5);		
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(2);
		
	}

	private View addSigns(int signsType, int numberOfSignsPerLine) {
		LinearLayout signsView = new LinearLayout(getBaseContext());
		signsView.setOrientation(LinearLayout.VERTICAL);
		ArrayList<ImageButton> buttons=new ArrayList<ImageButton>();
		for (int i = 0; i < mSigns.size(); i++) {
			final Sign sign = mSigns.get(i);
			if(sign.getSignType()==signsType){
				int signIcon=findDrawbleId(sign.getSignUri());
				ImageButton button = new ImageButton(getBaseContext());				
				button.setImageResource(signIcon);	
				button.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {		
						//TODO:Add SignExplanationActivity						 
						Intent openSignExplainerIntent = new Intent(getBaseContext(),SignExplanationActivity.class);
						int signIconBig=findDrawbleId(sign.getSignUriBig());
						Bundle bundle = new Bundle();
						bundle.putInt("icon", signIconBig);
						bundle.putString("gr", sign.getSignGrDesc());
						//bundle.putString("eng", sign.getSignEngDesc());
						openSignExplainerIntent.putExtras(bundle);							
						startActivity(openSignExplainerIntent);
					}
				});
				LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
				button.setLayoutParams(params);
				buttons.add(button);				
			}					
		}
		for(int i=0;i<buttons.size();i++){
			LinearLayout buttonRow=new LinearLayout(getBaseContext());
			for(int j=0; j<numberOfSignsPerLine && i<buttons.size();j++){
				buttonRow.addView(buttons.get(i));
				i++;
			}
			signsView.addView(buttonRow);			
		}		
		return signsView;
	}

	/**
	 * Loads the XML into the {@see mSigns} class member variable
	 * 
	 * @param startQuestionNumber
	 *            TODO: currently unused
	 * @throws XmlPullParserException
	 *             Thrown if XML parsing errors
	 * @throws IOException
	 *             Thrown if errors loading XML
	 */
	private ArrayList<Sign> loadSigns() throws XmlPullParserException,
			IOException {
		ArrayList<Sign> mSigns = new ArrayList<Sign>();
		XmlPullParser signsPacket;

		signsPacket = getResources().getXml(R.xml.signs);

		// Parse the XML
		int eventType = -1;

		// Find Score records from XML
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {

				// Get the name of the tag (eg questions or question)
				String strName = signsPacket.getName();

				if (strName.equals("sign")) {
					// String signId = signsPacket.getAttributeValue(null,
					// "sid");

					/*
					 * TODO: Check if the XmlPullParser Implementation package
					 * (kxml2-2.0.3.jar) is needed or it exists an
					 * implementation on android library
					 */
					// Get Sign Type
					int signType = Integer.parseInt(signsPacket
							.getAttributeValue(null, "signType"));

					signsPacket.nextTag();
					// String name=signsPacket.getName();
					String signUri = signsPacket.nextText().replace("\n", "");
					// Integer signIdNum = new Integer(signUri);
					signsPacket.nextTag();
					// String name2=signsPacket.getName();
					String signGrDesc = signsPacket.nextText();
					signsPacket.nextTag();
					// String name3=signsPacket.getName();
					String signEngDesc = signsPacket.nextText();
					// Save data to our hashtable
					mSigns.add(new Sign(signUri, signGrDesc, signEngDesc,
							signType));
				}
			}
			eventType = signsPacket.next();
		}
		return mSigns;
	}

	// private void loadSigns(){
	// XmlPullParser parser= XmlPullParserFactory.newPullParser();
	// }

	private class Sign {
		private int signType;
		private String signUri;
		private String signUriBig; // Describes the the image with the high
									// resolution
		private String signGrDesc; // which is used in the explanation layout
		private String signEngDesc;

		/**
		 * Constructor with 3 parameters, which generates the value of the field
		 * signUriBig by adding the signUri and the word "big".
		 * 
		 * @param signUri
		 * @param signGrDesc
		 * @param signEngDesc
		 * @param signType
		 */

		public Sign(String signUri, String signGrDesc, String signEngDesc,
				int signType) {
			super();
			this.signUri = signUri;
			this.signUriBig = signUri + "big";
			this.signGrDesc = signGrDesc;
			this.signEngDesc = signEngDesc;
			this.signType = signType;
		}

		public String getSignUriBig() {
			return signUriBig;
		}

		public void setSignUriBig(String signUriBig) {
			this.signUriBig = signUriBig;
		}

		public int getSignType() {
			return signType;
		}

		public void setSignType(int signType) {
			this.signType = signType;
		}

		public String getSignUri() {
			return signUri;
		}

		public void setSignUri(String signUri) {
			this.signUri = signUri;
		}

		public String getSignGrDesc() {
			return signGrDesc;
		}

		public void setSignGrDesc(String signGrDesc) {
			this.signGrDesc = signGrDesc;
		}

		public String getSignEngDesc() {
			return signEngDesc;
		}

		public void setSignEngDesc(String signEngDesc) {
			this.signEngDesc = signEngDesc;
		}

	}

	/**
	 * This method return the id of the drawable ex. you provide name of the
	 * file as String, KOK_84.png and method will seek in the R.drawable for the
	 * id of the image
	 * 
	 * @param String
	 *            drawableName
	 * 
	 * @return int id
	 */
	public static int findDrawbleId(String drawableName) {
		int id = 0;
		try {
			Field f = R.drawable.class.getField(drawableName);
			id = Integer.parseInt(f.get(new Object()).toString());

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int a=6;
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			signsLinearLayoutCreate(a);
		}
	}
	
	

}
