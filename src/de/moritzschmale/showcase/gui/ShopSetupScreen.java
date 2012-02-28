package de.moritzschmale.showcase.gui;

import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.narrowtux.gui.GenericCheckBox;
import com.narrowtux.narrowtuxlib.gui.Clickable;

import de.moritzschmale.showcase.ShowcaseMain;
import de.moritzschmale.showcase.types.ShopShowcaseExtra;

public class ShopSetupScreen extends GenericPopup implements Clickable {

	private SpoutPlayer player;
	private ShopShowcaseExtra extra;
	private Container 	container = new GenericContainer(),
						paymentList = new GenericContainer();
	private GenericCheckBox finite;
	private Button add = new GenericButton("+"), remove = new GenericButton("-"), edit = new GenericButton("Edit"), submit = new GenericButton("Done");
	
	public ShopSetupScreen(SpoutPlayer player, ShopShowcaseExtra extra){
		this.player = player;
		InGameHUD screen = player.getMainScreen();
		container.setHeight(screen.getHeight()).setWidth(screen.getWidth()).setX(0).setY(0);
		container.setLayout(ContainerType.VERTICAL);
		attachWidget(ShowcaseMain.instance, container);
		Label title = new GenericLabel("Shop Setup");
		title.setHeight(20).setWidth(150);
		container.addChild(title);
		
		finite = new GenericCheckBox();
		finite.setText("Finite");
		finite.setChecked(true);
		finite.setHeight(20).setWidth(150).setY(20);
		container.addChild(finite);
		
		//PAYMENT LIST
		container.addChild(paymentList);
		paymentList.setLayout(ContainerType.VERTICAL);
		
		Container paymentEditBar = new GenericContainer();
		container.addChild(paymentEditBar);
		paymentEditBar.setLayout(ContainerType.HORIZONTAL);
		paymentEditBar.addChildren(add, remove, edit);
		container.addChild(submit);
		container.updateLayout();
		
	}

	@Override
	public void onClick(Button btn) {
		// TODO Auto-generated method stub
		
	}
}
