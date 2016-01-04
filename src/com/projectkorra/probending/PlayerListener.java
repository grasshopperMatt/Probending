package com.projectkorra.probending;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.probending.objects.Team;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlayerListener implements Listener {

	Probending plugin;

	public PlayerListener(Probending plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW) 
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		List<String> allowedMoves = Probending.plugin.getConfig().getStringList("TeamSettings.AllowedMoves");
		String playerTeam = PBMethods.getPlayerTeam(p.getUniqueId());
		if (playerTeam != null) {
			if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne) || playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
				if (PBMethods.matchStarted) {
					if (!allowedMoves.contains(GeneralMethods.getBoundAbility(p).toString()) && GeneralMethods.getBoundAbility(p) != null) {
						p.sendMessage(PBMethods.Prefix + PBMethods.MoveNotAllowed.replace("%ability", GeneralMethods.getBoundAbility(p).toString()));
						e.setCancelled(true);
					}
				}
			}
		}
		if (PBMethods.allowedZone.containsKey(p.getName())) {
			if (PBMethods.matchStarted && PBMethods.getWorldGuard() != null && PBMethods.AutomateMatches) {
				Location loc = p.getLocation();
				Set<String> regions = PBMethods.RegionsAtLocation(loc);
				String allowedZone = PBMethods.allowedZone.get(p.getName());
				if (regions != null && !regions.isEmpty()) {
					if (!regions.contains(allowedZone)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (PBMethods.teamOnePlayers.contains(player.getName()) || PBMethods.teamTwoPlayers.contains(player.getName())) {
				e.setDamage(0);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerShift(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		List<String> allowedMoves = Probending.plugin.getConfig().getStringList("TeamSettings.AllowedMoves");
		String playerTeam = PBMethods.getPlayerTeam(p.getUniqueId());
		if (playerTeam != null) {
			if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne) || playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
				if (PBMethods.matchStarted) {
					if (!allowedMoves.contains(GeneralMethods.getBoundAbility(p).toString()) && GeneralMethods.getBoundAbility(p) != null) {
						e.setCancelled(true);
					}
				}
			}
		}
		if (PBMethods.allowedZone.containsKey(p.getName())) {
			if (PBMethods.matchStarted && PBMethods.getWorldGuard() != null && PBMethods.AutomateMatches) {
				Location loc = p.getLocation();
				Set<String> regions = PBMethods.RegionsAtLocation(loc);
				String allowedZone = PBMethods.allowedZone.get(p.getName());
				if (regions != null && !regions.isEmpty()) {
					if (!regions.contains(allowedZone)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerAnimation(PlayerAnimationEvent e) {
		Player p = e.getPlayer();
		List<String> allowedMoves = Probending.plugin.getConfig().getStringList("TeamSettings.AllowedMoves");
		String playerTeam = PBMethods.getPlayerTeam(p.getUniqueId());
		if (playerTeam != null) {
			if (playerTeam.equalsIgnoreCase(PBMethods.TeamOne) || playerTeam.equalsIgnoreCase(PBMethods.TeamTwo)) {
				if (PBMethods.matchStarted) {
					if (PBMethods.allowedZone.containsKey(p)) {

					}
					if (!allowedMoves.contains(GeneralMethods.getBoundAbility(p).toString()) && GeneralMethods.getBoundAbility(p) != null) {
						e.setCancelled(true);
					}
				}
			}
		}
		if (PBMethods.allowedZone.containsKey(p.getName())) {
			if (PBMethods.matchStarted && PBMethods.getWorldGuard() != null && PBMethods.AutomateMatches) {
				Location loc = p.getLocation();
				Set<String> regions = PBMethods.RegionsAtLocation(loc);
				String allowedZone = PBMethods.allowedZone.get(p.getName());
				if (regions != null && !regions.isEmpty()) {
					if (!regions.contains(allowedZone)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();

		if (PBMethods.allowedZone.containsKey(p.getName())) {
			if (PBMethods.matchStarted && PBMethods.getWorldGuard() != null && PBMethods.AutomateMatches) {
				String teamSide = null;

				if (PBMethods.getPlayerTeam(p.getUniqueId()) != null) {
					if (PBMethods.getPlayerTeam(p.getUniqueId()).equalsIgnoreCase(PBMethods.TeamOne)) teamSide = PBMethods.TeamOne;
					if (PBMethods.getPlayerTeam(p.getUniqueId()).equalsIgnoreCase(PBMethods.TeamTwo)) teamSide = PBMethods.TeamTwo; 
				}

				if (teamSide != null) { // Player is in a match.
					p.sendMessage(PBMethods.Prefix + PBMethods.CantTeleportDuringMatch);
					e.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PBMethods.allowedZone.containsKey(p.getName())) {
			if (PBMethods.matchStarted && PBMethods.getWorldGuard() != null && PBMethods.AutomateMatches) {
				String teamSide = null;
				if (PBMethods.getPlayerTeam(p.getUniqueId()) != null) {
					if (PBMethods.getPlayerTeam(p.getUniqueId()).equalsIgnoreCase(PBMethods.TeamOne)) teamSide = PBMethods.TeamOne;
					if (PBMethods.getPlayerTeam(p.getUniqueId()).equalsIgnoreCase(PBMethods.TeamTwo)) teamSide = PBMethods.TeamTwo;
				}

				if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
					PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", p.getName()));
					PBMethods.allowedZone.remove(p.getName());
					p.getInventory().setArmorContents(null);
					p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
					p.teleport(PBMethods.getSpectatorSpawn());
					Commands.tmpArmor.remove(p);
					if (PBMethods.teamOnePlayers.isEmpty()) {
						PBMethods.sendPBChat(PBMethods.RoundStopped);
						PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team", PBMethods.TeamTwo));
						PBMethods.addWin(PBMethods.TeamTwo);
						PBMethods.addLoss(PBMethods.TeamOne);
						Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
						PBMethods.matchStarted = false;
						PBMethods.playingTeams.clear();
						PBMethods.TeamOne = null;
						PBMethods.TeamTwo = null;
						PBMethods.allowedZone.clear();
						PBMethods.restoreArmor();
					}
				}
				if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
					PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", p.getName()));
					PBMethods.allowedZone.remove(p.getName());
					p.getInventory().setArmorContents(null);
					p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
					p.teleport(PBMethods.getSpectatorSpawn());
					Commands.tmpArmor.remove(p);
					if (PBMethods.teamTwoPlayers.isEmpty()) {
						PBMethods.sendPBChat(PBMethods.RoundStopped);
						PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team", PBMethods.TeamOne));
						PBMethods.addWin(PBMethods.TeamOne);
						PBMethods.addLoss(PBMethods.TeamTwo);
						Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
						PBMethods.matchStarted = false;
						PBMethods.playingTeams.clear();
						PBMethods.TeamOne = null;
						PBMethods.TeamTwo = null;
						PBMethods.allowedZone.clear();
						PBMethods.restoreArmor();
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Set<String> fromRegions = PBMethods.RegionsAtLocation(from);
		Set<String> toRegions = PBMethods.RegionsAtLocation(to);
		String teamSide = null;
		if (PBMethods.allowedZone.containsKey(p.getName())) { 

			if (PBMethods.matchStarted && PBMethods.getWorldGuard() != null && PBMethods.AutomateMatches) {

				if (PBMethods.getPlayerTeam(p.getUniqueId()) != null) {
					if (PBMethods.getPlayerTeam(p.getUniqueId()).equalsIgnoreCase(PBMethods.TeamOne)) teamSide = PBMethods.TeamOne;
					if (PBMethods.getPlayerTeam(p.getUniqueId()).equalsIgnoreCase(PBMethods.TeamTwo))teamSide = PBMethods.TeamTwo; 
				}

				if (PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t2z3)) {
					if (!toRegions.contains(PBMethods.t2z3)|| toRegions.isEmpty()) {
						if (fromRegions.contains(PBMethods.t2z3)) {
							PBMethods.allowedZone.remove(p.getName());
							PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", p.getName()));
							p.getInventory().setArmorContents(null);
							p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
							p.teleport(PBMethods.getSpectatorSpawn());
							Commands.tmpArmor.remove(p);
							PBMethods.teamTwoPlayers.remove(p.getName());
							if (PBMethods.teamTwoPlayers.isEmpty()) {
								PBMethods.sendPBChat(PBMethods.RoundStopped);
								PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team", PBMethods.TeamOne));
								PBMethods.addWin(PBMethods.TeamOne);
								PBMethods.addLoss(PBMethods.TeamTwo);
								Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
								PBMethods.matchStarted = false;
								PBMethods.playingTeams.clear();
								PBMethods.TeamOne = null;
								PBMethods.TeamTwo = null;
								PBMethods.allowedZone.clear();
								PBMethods.restoreArmor();
							}
						}
					}
					return;
				}
				if (PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z3)) {
					if (!toRegions.contains(PBMethods.t1z3)|| toRegions.isEmpty()) {
						if (fromRegions.contains(PBMethods.t1z3)) {
							PBMethods.allowedZone.remove(p.getName());
							PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", p.getName()));
							p.getInventory().setArmorContents(null);
							p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
							p.teleport(PBMethods.getSpectatorSpawn());
							Commands.tmpArmor.remove(p);
							PBMethods.teamOnePlayers.remove(p.getName());
							if (PBMethods.teamOnePlayers.isEmpty()) {
								PBMethods.sendPBChat(PBMethods.RoundStopped);
								PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team", PBMethods.TeamTwo));
								PBMethods.addWin(PBMethods.TeamTwo);
								PBMethods.addLoss(PBMethods.TeamOne);
								Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
								PBMethods.matchStarted = false;
								PBMethods.playingTeams.clear();
								PBMethods.TeamOne = null;
								PBMethods.TeamTwo = null;
								PBMethods.allowedZone.clear();
								PBMethods.restoreArmor();
							}
						}
					}
					return;                                                            
				}
				if (toRegions.contains(PBMethods.t2z3)) {
					if (!PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t2z3)) {
						// Check Team Two Zone 2
						if (fromRegions.contains(PBMethods.t2z2) && PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t2z2)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z1);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z2) && PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}

							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z3);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t2z2) && PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t2z1) && PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z1)) { 
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
						}
					}
				}
				if (toRegions.contains(PBMethods.t1z3)) {
					if (!PBMethods.allowedZone.containsKey(p.getName())) return;
					if (!PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z3)) {
						// Check Team One Zone 2
						if (fromRegions.contains(PBMethods.t1z2) && PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z2)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z3);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t1z2) && PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t1z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}

							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z1);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z2)) { 
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
						}
					}
				}

				if (toRegions.contains(PBMethods.t1z2)) {
					if (!PBMethods.allowedZone.containsKey(p.getName())) return;
					if (!PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z2)) {
						// Check Team One Zone One
						if (fromRegions.contains(PBMethods.t1z1) && PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z1)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z2);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t1z1)) { 
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z1);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z1) && PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z2)) {
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
						}
						// Check Team One Zone Three
						if (fromRegions.contains(PBMethods.t1z3) && PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z3)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
								PBMethods.allowedZone.remove(p.getName());
								PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", p.getName()));
								p.getInventory().setArmorContents(null);
								p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
								Commands.tmpArmor.remove(p);
								p.teleport(PBMethods.getSpectatorSpawn());
								PBMethods.teamOnePlayers.remove(p.getName());
								if (PBMethods.teamOnePlayers.isEmpty()) {
									PBMethods.sendPBChat(PBMethods.RoundStopped);
									PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team", PBMethods.TeamTwo));
									PBMethods.addWin(PBMethods.TeamTwo);
									PBMethods.addLoss(PBMethods.TeamOne);
									PBMethods.matchStarted = false;
									Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
									PBMethods.playingTeams.clear();
									PBMethods.TeamOne = null;
									PBMethods.TeamTwo = null;
									PBMethods.allowedZone.clear();
									PBMethods.restoreArmor();
								}
							}
						}

					}
				}
				if (toRegions.contains(PBMethods.t2z2)) {
					if (!PBMethods.allowedZone.containsKey(p.getName())) return;
					if (!PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t2z2)) {
						// Check TeamTwoZoneOne
						if (fromRegions.contains(PBMethods.t2z1)&& PBMethods.allowedZone.get(p.getName()).equals(PBMethods.t2z1)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z2);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t2z1) && PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)){
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z1);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z1) && PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z2)) { 
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}
						}
						// Check TeamTwoZoneThree
						if (fromRegions.contains(PBMethods.t2z3) && PBMethods.allowedZone.get(p.getName()).equals(PBMethods.t2z3)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.remove(p.getName());
								PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", p.getName()));
								p.getInventory().setArmorContents(null);
								p.getInventory().setArmorContents(Commands.tmpArmor.get(p));
								Commands.tmpArmor.remove(p);
								PBMethods.teamTwoPlayers.remove(p.getName());
								if (PBMethods.teamTwoPlayers.isEmpty()) {
									PBMethods.sendPBChat(PBMethods.RoundStopped);
									PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team", PBMethods.TeamOne));
									PBMethods.addWin(PBMethods.TeamOne);
									PBMethods.addLoss(PBMethods.TeamTwo);
									PBMethods.matchStarted = false;
									Bukkit.getServer().getScheduler().cancelTask(Commands.clockTask);
									PBMethods.playingTeams.clear();
									PBMethods.TeamOne = null;
									PBMethods.TeamTwo = null;
									PBMethods.allowedZone.clear();
									PBMethods.restoreArmor();
								}
							}
						}
					}
				}
				if (toRegions.contains(PBMethods.t2z1)) {
					if (!PBMethods.allowedZone.containsKey(p.getName())) return;
					if (!PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t2z1)) {
						// Check Team Two Zone Two
						if (fromRegions.contains(PBMethods.t2z2) && PBMethods.allowedZone.get(p.getName()).equals(PBMethods.t2z2)) {
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z3); 
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t2z2) && PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t2z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z1);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z2) && PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}
						}
						// Check Team One Zone One
						if (fromRegions.contains(PBMethods.t1z1) && PBMethods.allowedZone.get(p.getName()).equals(PBMethods.t1z1)) { // They are coming from Team One Zone One
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z2);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t1z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z1);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
						}
					}
				}
				if (toRegions.contains(PBMethods.t1z1)) {
					if (!PBMethods.allowedZone.containsKey(p.getName())) return;
					if (!PBMethods.allowedZone.get(p.getName()).equalsIgnoreCase(PBMethods.t1z1)) {
						//Check Team One Zone Two
						if (fromRegions.contains(PBMethods.t1z2) && PBMethods.allowedZone.get(p.getName()).equals(PBMethods.t1z2)) { // They are coming from Team One's Second Zone.
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) { // They are on Team One
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z3); // They were fouled back to Zone 3.
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t1z2)) { 
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) { // They are on Team Two
								PBMethods.allowedZone.put(p.getName(), PBMethods.t1z1); // Send them back to Team One Zone 1.
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t1z2)) {
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
						}
						// Check Team Two Zone One
						if (fromRegions.contains(PBMethods.t2z1) && PBMethods.allowedZone.get(p.getName()).equals(PBMethods.t2z1)) { // They are coming from Team Two's First Zone.
							if (teamSide.equalsIgnoreCase(PBMethods.TeamOne)) { // Team One Player
								PBMethods.allowedZone.put(p.getName(),  PBMethods.t1z1); // Stick them to Zone One on Team One's Side.
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z1) && PBMethods.isZoneEmpty(PBMethods.TeamOne, PBMethods.t2z2)) { 
									PBMethods.MovePlayersUp(PBMethods.TeamTwo, "Two");
								}
							}
							if (teamSide.equalsIgnoreCase(PBMethods.TeamTwo)) {
								PBMethods.allowedZone.put(p.getName(), PBMethods.t2z2);
								PBMethods.sendPBChat(PBMethods.PlayerFouled.replace("%player", p.getName()).replace("%zone", PBMethods.allowedZone.get(p.getName())));
								if (PBMethods.isZoneEmpty(PBMethods.TeamTwo, PBMethods.t2z1)) {
									PBMethods.MovePlayersUp(PBMethods.TeamOne, "One");
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Location loc = player.getLocation();
		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
			ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
			for (ProtectedRegion region: set) {
				if (region != null) {
					if (region.getId().equalsIgnoreCase(PBMethods.ProbendingField)) {
						if (PBMethods.buildDisabled) {
							if (!player.hasPermission("probending.worldguard.buildonfield")) {
								e.setCancelled(true);
							}
						}
					}

				}
			}
		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Entity entity = e.getWhoClicked();

		if (entity instanceof Player) {
			Player p = (Player) entity;

			if (Commands.tmpArmor.containsKey(p)) { // Don't let them throw away out armor if we're storing theirs!
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Location loc = player.getLocation();
		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
			ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
			for (ProtectedRegion region: set) {
				if (region != null) {
					if (region.getId().equalsIgnoreCase(PBMethods.ProbendingField)) {
						if (PBMethods.buildDisabled) {
							if (!player.hasPermission("probending.worldguard.buildonfield")) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public static void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location locTo = e.getTo();
		Location locFrom = e.getFrom();
		if (PBMethods.isWorldGuardSupportEnabled() && PBMethods.hasWorldGuard()) {
			ApplicableRegionSet set = WGBukkit.getRegionManager(locTo.getWorld()).getApplicableRegions(locTo);
			for (ProtectedRegion region: set) {
				if (region != null) {
					// Checks if the player can enter the field during a Probending Match
					if (region.getId().equalsIgnoreCase(PBMethods.ProbendingField)) {
						if (PBMethods.matchStarted) {
							String teamName = PBMethods.getPlayerTeam(player.getUniqueId());
							if (teamName != null) {
								if (!PBMethods.playingTeams.contains(teamName.toLowerCase())) {
									player.sendMessage(PBMethods.Prefix + PBMethods.CantEnterField);
									player.teleport(locFrom);
									e.setCancelled(true);
								}
							}
							if (teamName == null) {
								player.sendMessage(PBMethods.Prefix + PBMethods.CantEnterField);
								player.teleport(locFrom);
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent e) {
		if (Commands.pbChat.contains(e.getPlayer())) {
			e.getRecipients().clear();
			for (Player player: Bukkit.getOnlinePlayers()) {
				if (Commands.pbChat.contains(player)) {
					e.getRecipients().add(player);
				}
			}
			e.setFormat(PBMethods.Prefix + e.getFormat());
		}
	} 

	@EventHandler
	public void onChange(PlayerChangeElementEvent e) {
		Player player = e.getTarget();
		String t = PBMethods.getPlayerTeam(player.getUniqueId());
		Team team = PBMethods.getTeam(t);
		if (team != null) {
			String playerElementInTeam = PBMethods.getPlayerElementInTeam(player.getUniqueId(), t);
			if (playerElementInTeam != null) {
				String playerElement = null;
				if (GeneralMethods.isBender(player.getName(), Element.Air)) {
					playerElement = "Air";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Water)) {
					playerElement = "Water";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Earth)) {
					playerElement = "Earth";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Fire)) {
					playerElement = "Fire";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Chi)) {
					playerElement = "Chi";
				}
				if (!playerElementInTeam.equals(playerElement)) {
					player.sendMessage(PBMethods.Prefix + PBMethods.RemovedFromTeamBecauseDifferentElement);
					PBMethods.removePlayerFromTeam(team, player.getUniqueId(), playerElementInTeam);
					Set<String> teamElements = PBMethods.getTeamElements(t);
					if (teamElements.contains("Air")) {
						UUID airbender = team.getAirbender();
						team.setOwner(airbender);
						return;
					}
					if (teamElements.contains("Water")) {
						UUID bender = team.getWaterbender();
						team.setOwner(bender);
						return;
					}
					if (teamElements.contains("Earth")) {
						UUID bender = team.getEarthbender();
						team.setOwner(bender);
						return;
					}
					if (teamElements.contains("Fire")) {
						UUID bender = team.getFirebender();
						team.setOwner(bender);
						return;
					}
					if (teamElements.contains("Chi")) {
						UUID bender = team.getChiblocker();
						team.setOwner(bender);
						return;
					} else {
						PBMethods.deleteTeam(t);
					}
				}
			}
		}
	}
	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		PBMethods.createPlayer(player.getUniqueId());
		if (GeneralMethods.getBendingPlayer(player.getName()) == null) return;
		if (!(GeneralMethods.getBendingPlayer(player.getName()).getElements().size() > 1)) {
			String t = PBMethods.getPlayerTeam(player.getUniqueId());
			Team team = PBMethods.getTeam(t);
			if (team != null) {
				String playerElement = null;
				if (GeneralMethods.isBender(player.getName(), Element.Air)) {
					playerElement = "Air";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Water)) {
					playerElement = "Water";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Earth)) {
					playerElement = "Earth";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Fire)) {
					playerElement = "Fire";
				}
				if (GeneralMethods.isBender(player.getName(), Element.Chi)) {
					playerElement = "Chi";
				}
				String playerElementInTeam = PBMethods.getPlayerElementInTeam(player.getUniqueId(), t);
				if (playerElementInTeam != null) {
					if (!playerElementInTeam.equals(playerElement)) {
						player.sendMessage(PBMethods.Prefix + PBMethods.RemovedFromTeamBecauseDifferentElement);
						PBMethods.removePlayerFromTeam(team, player.getUniqueId(), playerElementInTeam);
						Set<String> teamElements = PBMethods.getTeamElements(t);
						if (teamElements.contains("Air")) {
							team.setOwner(team.getAirbender());
							return;
						}
						if (teamElements.contains("Water")) {
							team.setOwner(team.getWaterbender());
							return;
						}
						if (teamElements.contains("Earth")) {
							team.setOwner(team.getEarthbender());
							return;
						}
						if (teamElements.contains("Fire")) {
							team.setOwner(team.getFirebender());
							return;
						}
						if (teamElements.contains("Chi")) {
							team.setOwner(team.getChiblocker());
							return;
						} else {
							PBMethods.deleteTeam(t);
						}
					}
				}
			}
		}
	}
}