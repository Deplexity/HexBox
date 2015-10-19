package com.hexabeast.sandbox;

import java.util.ArrayList;

import com.hexabeast.hexboxserver.ConsoleMessage;
import com.hexabeast.hexboxserver.HMessage;
import com.hexabeast.hexboxserver.NBlockModification;
import com.hexabeast.hexboxserver.NInputRightLeft;
import com.hexabeast.hexboxserver.NInputUpDown;
import com.hexabeast.hexboxserver.NInputUpdate;
import com.hexabeast.hexboxserver.NPlayer;
import com.hexabeast.hexboxserver.NPlayerUpdate;
import com.hexabeast.hexboxserver.Nclick;
import com.hexabeast.hexboxserver.Ndead;

public class NetworkRequestList {
	
	private ArrayList<Object> modifications;
	
	public NetworkRequestList()
	{
		modifications = new ArrayList<Object>();
	}
	
	public synchronized void applyModifications()
	{
		for(int i = 0; i<modifications.size(); i++)
		{
			Object object = modifications.get(i);

			if(object instanceof NBlockModification)
			{
				NBlockModification modif = (NBlockModification)object;
				MapLayer layer = Map.instance.mainLayer;
				if(!modif.layer)layer = Map.instance.backLayer;
				
				if(modif.id != 0)ModifyTerrain.instance.setBlockFinal(modif.x, modif.y,modif.id, layer);
				else ModifyTerrain.instance.breakBlockFinal(modif.x, modif.y, layer);
			}
			else if (object instanceof NPlayer && Main.ingame)
	        {
				GameScreen.entities.mobs.NetworkPlayer(((NPlayer)object));
	        }
	          
			else if (object instanceof NPlayerUpdate && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkPlayerUpdate(((NPlayerUpdate)object));
	        }
			else if (object instanceof NInputUpdate && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkInputUpdate(((NInputUpdate)object));
	        }
			else if (object instanceof Nclick && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkClickUpdate(((Nclick)object));
	        }
			else if (object instanceof Ndead && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkDead(((Ndead)object));
	        }
			
			else if (object instanceof NInputRightLeft && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkRightLeft(((NInputRightLeft)object));
	        }
			
			else if (object instanceof NInputUpDown && Main.ingame)
	        {
	        	GameScreen.entities.mobs.NetworkUpDown(((NInputUpDown)object));
	        }
			
			else if (object instanceof HMessage && Main.ingame)
	        {
				Main.game.chat.addMessageN((HMessage)object);
	        }
			else if (object instanceof ConsoleMessage && Main.ingame)
	        {
				ConsoleMessage cm = (ConsoleMessage)object;
				if(cm.type == ConsoleMessage.DISCONNECTED)
				{
					HMessage m = new HMessage("Player " + cm.str + " has disconnected","Server");
					m.id = cm.id;
					Main.game.chat.addMessageN(m);
				}
				else if(cm.type == ConsoleMessage.CONNECTED)
				{
					HMessage m = new HMessage("Player " + cm.str + " has connected","Server");
					m.id = cm.id;
					Main.game.chat.addMessageN(m);
				}
				
	        }
		}
		modifications.clear();
	}
	
	public synchronized void add(Object object)
	{
		modifications.add(object);
	}

}
