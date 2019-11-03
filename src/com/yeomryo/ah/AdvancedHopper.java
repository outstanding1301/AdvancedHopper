package com.yeomryo.ah;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_5_R3.ItemSaddle;



public class AdvancedHopper extends JavaPlugin{
	
	public static List<HopperData> hl = new ArrayList<>();
	public static String pf = "§f§l[ §b§lAdvancedHopper §f§l]";
	
	public void load(){
		YamlConfiguration yc = YConfig.getYML(getDataFolder(), "config.yml");
		int i;
		int num = yc.getInt("Num");
		for(i=1;i<=num;i++){
			HopperData hd = new HopperData();
			World w = Bukkit.getWorld(yc.getString(i+".World"));
			if(w != null){
				hd.setHopper(w.getBlockAt(yc.getInt(i+".Hopper.X"), yc.getInt(i+".Hopper.Y"), yc.getInt(i+".Hopper.Z")));
				hd.setSign(w.getBlockAt(yc.getInt(i+".Sign.X"), yc.getInt(i+".Sign.Y"), yc.getInt(i+".Sign.Z")));
				hd.setItems(yc.getIntegerList(i+".Items"));
				hl.add(hd);
			}
		}
	}
	public void save(){
		YamlConfiguration yc = YConfig.getYMLforsave(getDataFolder(), "config.yml");
		int i=0;
		for(HopperData hd : hl){
			i++;
			yc.set("Num", i);
			yc.set(i+".World", hd.getHopper().getWorld().getName());
			yc.set(i+".Hopper.X", hd.getHopper().getLocation().getBlockX());
			yc.set(i+".Hopper.Y", hd.getHopper().getLocation().getBlockY());
			yc.set(i+".Hopper.Z", hd.getHopper().getLocation().getBlockZ());
			yc.set(i+".Sign.X", hd.getSign().getLocation().getBlockX());
			yc.set(i+".Sign.Y", hd.getSign().getLocation().getBlockY());
			yc.set(i+".Sign.Z", hd.getSign().getLocation().getBlockZ());
			yc.set(i+".Items", hd.getItems());
		}
		try {
			yc.save(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEnable() {
		load();
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("[ AdvancedHopper ] 제작 : 염료 (yeomryo@naver.com)");
		System.out.println("[ AdvancedHopper ] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("[ AdvancedHopper ] 플러그인이 활성화 되었습니다.");
		System.out.println("[ AdvancedHopper ] 본 플러그인의 저작권은 모두 염료에게 있습니다.");
		System.out.println(" ");
		System.out.println("[ AdvancedHopper ] 깔때기 정보를 불러왔습니다.");
		System.out.println("[ AdvancedHopper ] 등록된 깔때기 : "+hl.size());
		System.out.println(" ");
		System.out.println("[ AdvancedHopper ] 기본 라이센스 - 상업적 이용, 배포가 불가능합니다.");
		System.out.println(" ");
		System.out.println(" ");
		getServer().getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void onPlaceBlock(BlockPlaceEvent e){
				if(!e.isCancelled()){
					if(e.getBlock().getType() == Material.WALL_SIGN){
						Sign s = (Sign)e.getBlock().getState();
						if(e.getBlockAgainst().getType() == Material.HOPPER){
							Hopper h = (Hopper)e.getBlockAgainst().getState();
							HopperData hd = null;
							for(HopperData hdd : hl){
								if(hdd.getHopper().getLocation().distance(h.getLocation())==0){
									hd=hdd;
									break;
								}
							}
							if(hd != null){
								if(hd.getSign() != null){
									hd.getSign().breakNaturally();
								}
								hd.setSign(null);
								hd.setItems(new ArrayList<>());
							}else{
								hd = new HopperData();
								hl.add(hd);
							}
							hd.setHopper(h.getBlock());
							hd.setSign(s.getBlock());
							save();
						}
					}
				}
			}
			
			@EventHandler
			public void onSignADA(SignChangeEvent e){
				Sign s = (Sign)e.getBlock().getState();
				for(HopperData hdd : hl){
					if(hdd.getSign().getLocation().distance(s.getLocation())==0){
						if(e.getLine(0).equalsIgnoreCase("[분류하기]") || e.getLine(0).equalsIgnoreCase("[hopper]")){
							e.setLine(0, "§f[§a분류하기§f]");
							boolean a=true;
							for(String l : e.getLines()){
								if(a){
									a=false;
									continue;
								}
								try{
									int num = Integer.parseInt(l);
									hdd.getItems().add(num);
								}catch(NumberFormatException ee){
									e.getPlayer().sendMessage(pf+"2,3,4번째 줄에 아이템코드를 입력해주세요.");
									s.getBlock().breakNaturally();
									break;
								}
							}
							save();
							break;
						}else{
							e.getPlayer().sendMessage(pf+"맨 윗줄에 [분류하기]를 적어주세요.");
							s.getBlock().breakNaturally();
						}
					}
				}
			}
			
			@EventHandler
			public void onBreakBlock(BlockBreakEvent e){
				if(!e.isCancelled()){
					if(e.getBlock().getType() == Material.WALL_SIGN){
						Sign s = (Sign)e.getBlock().getState();
						for(HopperData hdd : hl){
							if(hdd.getSign().getLocation().distance(s.getLocation())==0){
								if(hdd.getHopper() != null)
									hdd.getHopper().breakNaturally();
								hdd.setSign(null);
								hdd.setHopper(null);
								hdd.getItems().clear();
								hl.remove(hdd);
								save();
								break;
							}
						}
					}
					if(e.getBlock().getType() == Material.HOPPER){
						Hopper h = (Hopper)e.getBlock().getState();
						for(HopperData hdd : hl){
							if(hdd.getHopper().getLocation().distance(h.getLocation())==0){
								hdd.setSign(null);
								hdd.setHopper(null);
								hdd.getItems().clear();
								hl.remove(hdd);
								save();
								break;
							}
						}
					}
				}
			}
			
			@EventHandler
			public void onItemMove(InventoryMoveItemEvent e){
				if(e.getDestination().getType() == InventoryType.HOPPER){
					Hopper h = (Hopper)e.getDestination().getHolder();
					for(HopperData hd : hl){
						if(hd.getHopper().getLocation().distance(h.getLocation())==0){
							if(!hd.getItems().contains(e.getItem().getTypeId())){
								new Thread(()->{ for(int i=0;i<Integer.MAX_VALUE;i++){}e.getInitiator().remove(e.getItem().getType()); }).start();
								e.setCancelled(true);
							}
						}
					}
				}
				if(e.getInitiator().getType() == InventoryType.HOPPER){
					Hopper h = (Hopper)e.getInitiator().getHolder();
					for(HopperData hd : hl){
						if(hd.getHopper().getLocation().distance(h.getLocation())==0){
							if(!hd.getItems().contains(e.getItem().getTypeId())){
								new Thread(()->{ for(int i=0;i<Integer.MAX_VALUE;i++){}e.getInitiator().remove(e.getItem().getType()); }).start();
								e.setCancelled(true);
							}
						}
					}
				}
			}
			
			@EventHandler
			public void onInventoryPickup(InventoryPickupItemEvent e){
				if(e.getInventory().getType() == InventoryType.HOPPER){
					Hopper h = (Hopper)e.getInventory().getHolder();
					for(HopperData hd : hl){
						if(hd.getHopper().getLocation().distance(h.getLocation())==0){
							if(!hd.getItems().contains(e.getItem().getItemStack().getTypeId())){
								e.setCancelled(true);
							}
						}
					}
				}
			}
			
		}, this);
	}
	@Override
	public void onDisable() {
		
	}
}
