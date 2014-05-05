package io.github.redinzane.realisticchat.minions;

import io.github.redinzane.realisticchat.RealisticChat;

import java.util.Collection;

import javax.persistence.PersistenceException;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;

public class RealisticChatDAO {
    
    private static final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
    private static final long ZERO = 0;
    private RealisticChat plugin;
    private EbeanServer db;

    public RealisticChatDAO(RealisticChat plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        db = plugin.getDatabase();
        try {
            db.find(ChatMessage.class).findRowCount();
        } catch (PersistenceException e) {
            plugin.installDDL();
            plugin.getLogger().info("No database found, creating new one.");
        }
    }

    public void log(ChatMessage msg) {
        if (msg != null) {
            db.save(msg);
        }
    }

    public ChatMessage findByCUUID(String UUID) {
        Query<ChatMessage> query = find().where().eq("cUUID", UUID).query();
        try {
            return query.findUnique();
        } catch (PersistenceException e) {
            return null;
        }
    }
    public ChatMessage findByPUUID(String UUID) {
        Query<ChatMessage> query = find().where().eq("pUUID", UUID).query();
        try {
            return query.findUnique();
        } catch (PersistenceException e) {
            return null;
        }
    }
    
    public void pruneDatabase() {
        Query<ChatMessage> query = find().where().between("timestamp", ZERO, System.currentTimeMillis()-ONE_WEEK).query();
        remove(query.findList());
    }
    
    public void remove(ChatMessage msg) {
        if (msg != null) {
            db.delete(msg);
        }
    }
    
    public void remove(Collection<ChatMessage> msgs) {
        db.delete(msgs);
    }

    private Query<ChatMessage> find() {
        return db.find(ChatMessage.class);
    }

}
