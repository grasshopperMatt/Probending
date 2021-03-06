package com.projectkorra.probending.objects;

import com.projectkorra.probending.PBMethods;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Round {

	public static ConcurrentHashMap<UUID, Round> rounds = new ConcurrentHashMap<UUID, Round>();

	UUID uuid;
	Team teamOne;
	Team teamTwo;
	Arena arena;
	Set<Player> teamOnePlayers;
	Set<Player> teamTwoPlayers;
	boolean isPaused;
	boolean isOnCountdown;
	long currTime;
	long countdownLength;
	long roundStartTime;
	long roundDuration;
	long duration;
	HashMap<Player, String> allowedZone;

	public Round(Team teamOne, Team teamTwo, Set<Player> teamOnePlayers, Set<Player> teamTwoPlayers, Arena arena) {
		this.uuid = UUID.randomUUID();
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		this.teamOnePlayers = teamOnePlayers;
		this.teamTwoPlayers = teamTwoPlayers;
		this.arena = arena;
		this.isPaused = false;
		this.isOnCountdown = true;
		this.countdownLength = 5000;
		this.roundDuration = 180000;
		this.currTime = System.currentTimeMillis();
		this.roundStartTime = System.currentTimeMillis();
		allowedZone = new HashMap<Player, String>();

		rounds.put(this.uuid, this);
	}

	public void setAllowedZone(Player player, String zone) {
		allowedZone.put(player, zone);
	}

	public String getAllowedZone(Player player) {
		return allowedZone.get(player);
	}

	public Team getTeamOne() {
		return this.teamOne;
	}

	public Team getTeamTwo() {
		return this.teamTwo;
	}

	public Arena getArena() {
		return this.arena;
	}

	public boolean isPaused() {
		return this.isPaused;
	}

	public boolean isZoneEmpty(Team team, String zone) {
		if (team == getTeamOne()) {
			for(Player player: teamOnePlayers) {
				if (allowedZone.get(player).contains(zone)) {
					return false;
				}
			}
		}

		if (team == getTeamTwo()) {
			for (Player player: teamTwoPlayers) {
				if (allowedZone.get(player).contains(zone)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Moves all players still in a Probending match on a specific team up one zone.
	 * @param team = Name of the team to move.
	 * @param side = What side they are on. (One or Two)
	 */

	public void movePlayersUp(Team team) {
		for (Player player: getRoundPlayers()) {
			if (!team.getPlayerUUIDs().contains(player.getUniqueId())) {
				continue;
			}
			String zone = getAllowedZone(player);
			if (this.teamOnePlayers.contains(player)) {
				if (zone.equalsIgnoreCase(arena.getTeamOneZoneOne())) { //Move player from t1z1 to t2z1
					setAllowedZone(player, arena.getTeamTwoZoneOne());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
				if (zone.equalsIgnoreCase(arena.getTeamTwoZoneOne())) { //Move player from t2z1 to t2z2
					setAllowedZone(player, arena.getTeamTwoZoneTwo());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
				if (zone.equalsIgnoreCase(arena.getTeamOneZoneTwo())) { //Move player from t1z2 to t1z1
					setAllowedZone(player, arena.getTeamOneZoneOne());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
				if (zone.equalsIgnoreCase(arena.getTeamOneZoneThree())) { //Move player from t1z3 to t1z2
					setAllowedZone(player, arena.getTeamOneZoneTwo());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
			}
			if (this.teamTwoPlayers.contains(player)) {
				if (zone.equalsIgnoreCase(arena.getTeamTwoZoneOne())) { //Move player from t2z1 to t1z1
					setAllowedZone(player, arena.getTeamOneZoneOne());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
				if (zone.equalsIgnoreCase(arena.getTeamTwoZoneTwo())) { //Move player from t2z2 to t2z1
					setAllowedZone(player, arena.getTeamTwoZoneOne());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
				if (zone.equalsIgnoreCase(arena.getTeamTwoZoneThree())) { //Move player from t2z3 to t2z2
					setAllowedZone(player, arena.getTeamTwoZoneTwo());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
				if (zone.equalsIgnoreCase(arena.getTeamOneZoneOne())) { //Move player from t1z1 to t1z2
					setAllowedZone(player, arena.getTeamOneZoneTwo());
					player.sendMessage(PBMethods.Prefix + PBMethods.MoveUpOneZone.replace("%zone", getAllowedZone(player)));
					continue;
				}
			}
		}
	}
	public void eliminatePlayer(Player player) {
		PBMethods.sendPBChat(PBMethods.PlayerEliminated.replace("%player", player.getName()), this);
		this.allowedZone.remove(player);
		PBMethods.restoreArmor(player);
		player.teleport(arena.getSpectatorSpawn());
		if (this.teamOnePlayers.contains(player)) {
			this.teamOnePlayers.remove(player);
			if (this.teamOnePlayers.isEmpty()) { // Team Two has won.
				getTeamTwo().addWin();
				getTeamOne().addLoss();
				PBMethods.sendPBChat(PBMethods.RoundStopped.replace("%arena", arena.getName()), null);
				PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team1", getTeamTwo().getName()).replace("%team2", getTeamOne().getName()), null);
				endRound();
			}

		}
		if (this.teamTwoPlayers.contains(player)) {
			this.teamTwoPlayers.remove(player);
			if (this.teamTwoPlayers.isEmpty()) { // Team One has won
				getTeamOne().addWin();
				getTeamTwo().addLoss();
				PBMethods.sendPBChat(PBMethods.RoundStopped.replace("%arena", arena.getName()), null);
				PBMethods.sendPBChat(PBMethods.TeamWon.replace("%team1", getTeamOne().getName()).replace("%team2", getTeamTwo().getName()), null);
				endRound();
			}
		}
	}

	public Set<Player> getRoundPlayers() {
		Set<Player> players = new HashSet<Player>();
		players.addAll(this.teamOnePlayers);
		players.addAll(this.teamTwoPlayers);
		return players;
	}

	public long getTimeCreated() {
		return this.currTime;
	}

	public long getCountdownLength() {
		return this.countdownLength;
	}

	public long getStartTime() {
		return this.roundStartTime;
	}

	public long getRoundDuration() {
		return this.roundDuration;
	}

	public void setStartTime(long time) {
		this.roundStartTime = time;
	}

	public static void progressAll() {
		for (UUID uuids: rounds.keySet()) {
			Round round = rounds.get(uuids);
			if (round.isOnCountdown) {
				if (System.currentTimeMillis() <= round.getTimeCreated() + round.getCountdownLength()) {
					round.isOnCountdown = false;
					PBMethods.sendPBChat(PBMethods.RoundStarted
							.replace("%team1", round.getTeamOne().getName())
							.replace("%team2", round.getTeamTwo().getName())
							.replace("%seconds", String.valueOf(round.getRoundDuration()/1000)), null);
					round.setStartTime(System.currentTimeMillis());
					continue;
				}
			}
			if (round.getStartTime() + round.getRoundDuration() <= System.currentTimeMillis()) {
				round.endRound();
			}
		}
	}
	
	public void endRound() {
		for (Player player: getRoundPlayers()) {
			player.teleport(getArena().getSpectatorSpawn());
			PBMethods.restoreArmor(player);
		}
		stop();
	}
	
	public void stop() {
		rounds.remove(this.uuid);
	}
}
