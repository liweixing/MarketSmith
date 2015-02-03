package com.marketsmith.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Class Name: DatabaseHelper 
 * Description:   TODO
 * @author  Wendy
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "MarketSmith.db";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		// try {
		// Log.i(DatabaseHelper.class.getName(), "onCreate");
		// TableUtils.createTable(connectionSource, UserInfoDB.class);
		// TableUtils.createTable(connectionSource, ProductDB.class);
		// } catch (SQLException e) {
		// Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
		// throw new RuntimeException(e);
		// }

	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// try {
		// Log.i(DatabaseHelper.class.getName(), "onUpgrade");
		// // after we drop the old databases, we create the new ones
		// TableUtils.dropTable(connectionSource, UserInfoDB.class, true);
		// TableUtils.dropTable(connectionSource, ProductDB.class, true);
		// mLog.debug("onUpgrade database success!");
		// onCreate(database, connectionSource);
		// } catch (SQLException e) {
		// Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
		// throw new RuntimeException(e);
		// }
	}

}
