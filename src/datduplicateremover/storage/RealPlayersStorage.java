/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package datduplicateremover.storage;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;

public class RealPlayersStorage {

	private HashMap<String, PlayerData> realplayers = new HashMap<String, PlayerData>();

	public RealPlayersStorage() {
		for (OfflinePlayer player : PlayersDataUtils.getPlayers()) {
			if (player.getName() == null) {
				continue;
			}
			if (!hasRealPlayer(player)) {
				addRealPlayer(player);
				continue;
			}
			if (player.getLastPlayed() > realplayers.get(player.getName().toLowerCase()).getLastPlayed()) {
				addRealPlayer(player);
				continue;
			}
		}
	}

	public boolean hasRealPlayer(OfflinePlayer player) {
		if (realplayers.containsKey(player.getName().toLowerCase())) {
			if (!PlayersDataUtils.getPlayerFile(player).exists()) {
				removeRealPlayer(player);
				return false;
			}
			return true;
		}
		return false;
	}

	public void addRealPlayer(OfflinePlayer player) {
		realplayers.put(player.getName().toLowerCase(), new PlayerData(player));
	}

	public boolean isPlayerReal(OfflinePlayer player) {
		if (hasRealPlayer(player)) {
			PlayerData playerdata = realplayers.get(player.getName().toLowerCase());
			return playerdata.getName().equals(player.getName());
		}
		return false;
	}

	public String getRealPlayerValidName(OfflinePlayer player) {
		return realplayers.get(player.getName().toLowerCase()).getName();
	}

	private void removeRealPlayer(OfflinePlayer player) {
		realplayers.remove(player.getName().toLowerCase());
	}

	private class PlayerData {

		private String name;
		private long lastPlayed;

		public PlayerData(OfflinePlayer player) {
			name = player.getName();
			lastPlayed = player.getLastPlayed();
		}

		public String getName() {
			return name;
		}

		public long getLastPlayed() {
			return lastPlayed;
		}

	}

}
