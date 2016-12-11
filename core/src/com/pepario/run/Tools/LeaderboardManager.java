package com.pepario.run.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.pepario.run.Config.Config;
import com.pepario.run.JsonObjects.LeaderboardEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * We do the leaderboards on a key basis (1 key per file)
 * due to it being easier to process the maximum items per key.
 *
 * Otherwise you would have to count every key and validate them all
 * instead of doing it once per file.
 */
public class LeaderboardManager
{
    public static void addLine(String key, String name, int score)
    {
        LeaderboardEntry entry = new LeaderboardEntry(key, name, score);

        // Obtain the current leaderboard from the json file
        List<LeaderboardEntry> leaderboardEntries = obtainLeaderboardEntriesForKey(key);

        // Add the new element to the leaderboard list
        leaderboardEntries.add(entry);

        // Sort the items (see the LeaderboardEntry::compareTo method for the sorting details)
        Collections.sort(leaderboardEntries);

        // This will be our array that contains the elements that we have to write to the json file
        List<LeaderboardEntry> entriesToWrite = new ArrayList<LeaderboardEntry>();

        // We only want to keep the amount of entries specified in the config as maximum
        // so we filter the exceeding entries away here.
        for(int i = 0; i < Config.LEADERBOARD_FILE_MAX_ENTRIES; i++) {
            if(i < leaderboardEntries.size()) {
                entriesToWrite.add(leaderboardEntries.get(i));
            }
        }

        Json json = new Json();

        FileHandle file = Gdx.files.local(getLeaderboardFileNameForKey(key));

        // Write our entries (should always be smaller or equal to the LEADERBOARD_FILE_MAX_ENTRIES flag
        // specified in the config
        file.writeString(json.prettyPrint(entriesToWrite), false);
    }

    public static boolean isLeaderboardWorthyScore(String key, int score)
    {
        List<LeaderboardEntry> entries = obtainLeaderboardEntriesForKey(key);

        // If there is room on the leaderboard, we will always welcome additions
        if(entries.size() < Config.LEADERBOARD_FILE_MAX_ENTRIES) {
            return true;
        }

        LeaderboardEntry lastEntry = entries.get(entries.size() - 1);

        // If the given score is higher then the latest entry, we will welcome additions
        // since we override the other score
        return score > lastEntry.getValue();
    }

    private static List<LeaderboardEntry> obtainLeaderboardEntriesForKey(String key)
    {
        FileHandle file = Gdx.files.local(getLeaderboardFileNameForKey(key));

        Json json = new Json();

        ArrayList<LeaderboardEntry> entries = new ArrayList<LeaderboardEntry>();

        // If the file does not exist, we will return an empty array.
        if(!file.exists()) {
            return entries;
        }

        // Read our file as an array of LeaderboardEntry objects
        LeaderboardEntry[] fileEntries = json.fromJson(LeaderboardEntry[].class, file.read());

        // FileEntries can be null when the file is empty, so if that is the case we will
        // not fill the array
        if(fileEntries != null) {
            // Add all our entries from the json file to the arraylist with the help of the
            // addAll method
            Collections.addAll(entries, fileEntries);
        }

        return entries;
    }

    private static String getLeaderboardFileNameForKey(String key)
    {
        return key + "_" + Config.LEADERBOARD_BASE_FILE_NAME;
    }
}
