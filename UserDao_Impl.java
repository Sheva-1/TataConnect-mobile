package com.example.tataconnect;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`uid`,`email`,`password`,`userType`,`isProfileComplete`,`isAvailable`,`fullName`,`location`,`phone`,`profilePicUrl`,`about`,`familySize`,`minBudget`,`maxBudget`,`servicesNeeded`,`experience`,`expectedSalary`,`skills`,`languages`,`availability`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUid());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPassword());
        }
        if (entity.getUserType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUserType());
        }
        final int _tmp = entity.isProfileComplete() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isAvailable() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getFullName() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFullName());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLocation());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPhone());
        }
        if (entity.getProfilePicUrl() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getProfilePicUrl());
        }
        if (entity.getAbout() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getAbout());
        }
        statement.bindLong(13, entity.getFamilySize());
        statement.bindLong(14, entity.getMinBudget());
        statement.bindLong(15, entity.getMaxBudget());
        if (entity.getServicesNeeded() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getServicesNeeded());
        }
        statement.bindLong(17, entity.getExperience());
        statement.bindLong(18, entity.getExpectedSalary());
        if (entity.getSkills() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getSkills());
        }
        if (entity.getLanguages() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getLanguages());
        }
        if (entity.getAvailability() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getAvailability());
        }
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `id` = ?,`uid` = ?,`email` = ?,`password` = ?,`userType` = ?,`isProfileComplete` = ?,`isAvailable` = ?,`fullName` = ?,`location` = ?,`phone` = ?,`profilePicUrl` = ?,`about` = ?,`familySize` = ?,`minBudget` = ?,`maxBudget` = ?,`servicesNeeded` = ?,`experience` = ?,`expectedSalary` = ?,`skills` = ?,`languages` = ?,`availability` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUid());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPassword());
        }
        if (entity.getUserType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUserType());
        }
        final int _tmp = entity.isProfileComplete() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isAvailable() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getFullName() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFullName());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLocation());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPhone());
        }
        if (entity.getProfilePicUrl() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getProfilePicUrl());
        }
        if (entity.getAbout() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getAbout());
        }
        statement.bindLong(13, entity.getFamilySize());
        statement.bindLong(14, entity.getMinBudget());
        statement.bindLong(15, entity.getMaxBudget());
        if (entity.getServicesNeeded() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getServicesNeeded());
        }
        statement.bindLong(17, entity.getExperience());
        statement.bindLong(18, entity.getExpectedSalary());
        if (entity.getSkills() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getSkills());
        }
        if (entity.getLanguages() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getLanguages());
        }
        if (entity.getAvailability() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getAvailability());
        }
        statement.bindLong(22, entity.getId());
      }
    };
  }

  @Override
  public long insert(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfUser.insertAndReturnId(user);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public User login(final String email, final String password) {
    final String _sql = "SELECT * FROM users WHERE email = ? AND password = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    _argIndex = 2;
    if (password == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, password);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "userType");
      final int _cursorIndexOfIsProfileComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "isProfileComplete");
      final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfProfilePicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "profilePicUrl");
      final int _cursorIndexOfAbout = CursorUtil.getColumnIndexOrThrow(_cursor, "about");
      final int _cursorIndexOfFamilySize = CursorUtil.getColumnIndexOrThrow(_cursor, "familySize");
      final int _cursorIndexOfMinBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "minBudget");
      final int _cursorIndexOfMaxBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "maxBudget");
      final int _cursorIndexOfServicesNeeded = CursorUtil.getColumnIndexOrThrow(_cursor, "servicesNeeded");
      final int _cursorIndexOfExperience = CursorUtil.getColumnIndexOrThrow(_cursor, "experience");
      final int _cursorIndexOfExpectedSalary = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedSalary");
      final int _cursorIndexOfSkills = CursorUtil.getColumnIndexOrThrow(_cursor, "skills");
      final int _cursorIndexOfLanguages = CursorUtil.getColumnIndexOrThrow(_cursor, "languages");
      final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpUid;
        if (_cursor.isNull(_cursorIndexOfUid)) {
          _tmpUid = null;
        } else {
          _tmpUid = _cursor.getString(_cursorIndexOfUid);
        }
        _result.setUid(_tmpUid);
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        _result.setEmail(_tmpEmail);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _result.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _result.setUserType(_tmpUserType);
        final boolean _tmpIsProfileComplete;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsProfileComplete);
        _tmpIsProfileComplete = _tmp != 0;
        _result.setProfileComplete(_tmpIsProfileComplete);
        final boolean _tmpIsAvailable;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsAvailable);
        _tmpIsAvailable = _tmp_1 != 0;
        _result.setAvailable(_tmpIsAvailable);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _result.setFullName(_tmpFullName);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpProfilePicUrl;
        if (_cursor.isNull(_cursorIndexOfProfilePicUrl)) {
          _tmpProfilePicUrl = null;
        } else {
          _tmpProfilePicUrl = _cursor.getString(_cursorIndexOfProfilePicUrl);
        }
        _result.setProfilePicUrl(_tmpProfilePicUrl);
        final String _tmpAbout;
        if (_cursor.isNull(_cursorIndexOfAbout)) {
          _tmpAbout = null;
        } else {
          _tmpAbout = _cursor.getString(_cursorIndexOfAbout);
        }
        _result.setAbout(_tmpAbout);
        final int _tmpFamilySize;
        _tmpFamilySize = _cursor.getInt(_cursorIndexOfFamilySize);
        _result.setFamilySize(_tmpFamilySize);
        final int _tmpMinBudget;
        _tmpMinBudget = _cursor.getInt(_cursorIndexOfMinBudget);
        _result.setMinBudget(_tmpMinBudget);
        final int _tmpMaxBudget;
        _tmpMaxBudget = _cursor.getInt(_cursorIndexOfMaxBudget);
        _result.setMaxBudget(_tmpMaxBudget);
        final String _tmpServicesNeeded;
        if (_cursor.isNull(_cursorIndexOfServicesNeeded)) {
          _tmpServicesNeeded = null;
        } else {
          _tmpServicesNeeded = _cursor.getString(_cursorIndexOfServicesNeeded);
        }
        _result.setServicesNeeded(_tmpServicesNeeded);
        final int _tmpExperience;
        _tmpExperience = _cursor.getInt(_cursorIndexOfExperience);
        _result.setExperience(_tmpExperience);
        final int _tmpExpectedSalary;
        _tmpExpectedSalary = _cursor.getInt(_cursorIndexOfExpectedSalary);
        _result.setExpectedSalary(_tmpExpectedSalary);
        final String _tmpSkills;
        if (_cursor.isNull(_cursorIndexOfSkills)) {
          _tmpSkills = null;
        } else {
          _tmpSkills = _cursor.getString(_cursorIndexOfSkills);
        }
        _result.setSkills(_tmpSkills);
        final String _tmpLanguages;
        if (_cursor.isNull(_cursorIndexOfLanguages)) {
          _tmpLanguages = null;
        } else {
          _tmpLanguages = _cursor.getString(_cursorIndexOfLanguages);
        }
        _result.setLanguages(_tmpLanguages);
        final String _tmpAvailability;
        if (_cursor.isNull(_cursorIndexOfAvailability)) {
          _tmpAvailability = null;
        } else {
          _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
        }
        _result.setAvailability(_tmpAvailability);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User getUserByEmail(final String email) {
    final String _sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "userType");
      final int _cursorIndexOfIsProfileComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "isProfileComplete");
      final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfProfilePicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "profilePicUrl");
      final int _cursorIndexOfAbout = CursorUtil.getColumnIndexOrThrow(_cursor, "about");
      final int _cursorIndexOfFamilySize = CursorUtil.getColumnIndexOrThrow(_cursor, "familySize");
      final int _cursorIndexOfMinBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "minBudget");
      final int _cursorIndexOfMaxBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "maxBudget");
      final int _cursorIndexOfServicesNeeded = CursorUtil.getColumnIndexOrThrow(_cursor, "servicesNeeded");
      final int _cursorIndexOfExperience = CursorUtil.getColumnIndexOrThrow(_cursor, "experience");
      final int _cursorIndexOfExpectedSalary = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedSalary");
      final int _cursorIndexOfSkills = CursorUtil.getColumnIndexOrThrow(_cursor, "skills");
      final int _cursorIndexOfLanguages = CursorUtil.getColumnIndexOrThrow(_cursor, "languages");
      final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpUid;
        if (_cursor.isNull(_cursorIndexOfUid)) {
          _tmpUid = null;
        } else {
          _tmpUid = _cursor.getString(_cursorIndexOfUid);
        }
        _result.setUid(_tmpUid);
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        _result.setEmail(_tmpEmail);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _result.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _result.setUserType(_tmpUserType);
        final boolean _tmpIsProfileComplete;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsProfileComplete);
        _tmpIsProfileComplete = _tmp != 0;
        _result.setProfileComplete(_tmpIsProfileComplete);
        final boolean _tmpIsAvailable;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsAvailable);
        _tmpIsAvailable = _tmp_1 != 0;
        _result.setAvailable(_tmpIsAvailable);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _result.setFullName(_tmpFullName);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpProfilePicUrl;
        if (_cursor.isNull(_cursorIndexOfProfilePicUrl)) {
          _tmpProfilePicUrl = null;
        } else {
          _tmpProfilePicUrl = _cursor.getString(_cursorIndexOfProfilePicUrl);
        }
        _result.setProfilePicUrl(_tmpProfilePicUrl);
        final String _tmpAbout;
        if (_cursor.isNull(_cursorIndexOfAbout)) {
          _tmpAbout = null;
        } else {
          _tmpAbout = _cursor.getString(_cursorIndexOfAbout);
        }
        _result.setAbout(_tmpAbout);
        final int _tmpFamilySize;
        _tmpFamilySize = _cursor.getInt(_cursorIndexOfFamilySize);
        _result.setFamilySize(_tmpFamilySize);
        final int _tmpMinBudget;
        _tmpMinBudget = _cursor.getInt(_cursorIndexOfMinBudget);
        _result.setMinBudget(_tmpMinBudget);
        final int _tmpMaxBudget;
        _tmpMaxBudget = _cursor.getInt(_cursorIndexOfMaxBudget);
        _result.setMaxBudget(_tmpMaxBudget);
        final String _tmpServicesNeeded;
        if (_cursor.isNull(_cursorIndexOfServicesNeeded)) {
          _tmpServicesNeeded = null;
        } else {
          _tmpServicesNeeded = _cursor.getString(_cursorIndexOfServicesNeeded);
        }
        _result.setServicesNeeded(_tmpServicesNeeded);
        final int _tmpExperience;
        _tmpExperience = _cursor.getInt(_cursorIndexOfExperience);
        _result.setExperience(_tmpExperience);
        final int _tmpExpectedSalary;
        _tmpExpectedSalary = _cursor.getInt(_cursorIndexOfExpectedSalary);
        _result.setExpectedSalary(_tmpExpectedSalary);
        final String _tmpSkills;
        if (_cursor.isNull(_cursorIndexOfSkills)) {
          _tmpSkills = null;
        } else {
          _tmpSkills = _cursor.getString(_cursorIndexOfSkills);
        }
        _result.setSkills(_tmpSkills);
        final String _tmpLanguages;
        if (_cursor.isNull(_cursorIndexOfLanguages)) {
          _tmpLanguages = null;
        } else {
          _tmpLanguages = _cursor.getString(_cursorIndexOfLanguages);
        }
        _result.setLanguages(_tmpLanguages);
        final String _tmpAvailability;
        if (_cursor.isNull(_cursorIndexOfAvailability)) {
          _tmpAvailability = null;
        } else {
          _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
        }
        _result.setAvailability(_tmpAvailability);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User getUserById(final int id) {
    final String _sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "userType");
      final int _cursorIndexOfIsProfileComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "isProfileComplete");
      final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfProfilePicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "profilePicUrl");
      final int _cursorIndexOfAbout = CursorUtil.getColumnIndexOrThrow(_cursor, "about");
      final int _cursorIndexOfFamilySize = CursorUtil.getColumnIndexOrThrow(_cursor, "familySize");
      final int _cursorIndexOfMinBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "minBudget");
      final int _cursorIndexOfMaxBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "maxBudget");
      final int _cursorIndexOfServicesNeeded = CursorUtil.getColumnIndexOrThrow(_cursor, "servicesNeeded");
      final int _cursorIndexOfExperience = CursorUtil.getColumnIndexOrThrow(_cursor, "experience");
      final int _cursorIndexOfExpectedSalary = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedSalary");
      final int _cursorIndexOfSkills = CursorUtil.getColumnIndexOrThrow(_cursor, "skills");
      final int _cursorIndexOfLanguages = CursorUtil.getColumnIndexOrThrow(_cursor, "languages");
      final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
      final User _result;
      if (_cursor.moveToFirst()) {
        _result = new User();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpUid;
        if (_cursor.isNull(_cursorIndexOfUid)) {
          _tmpUid = null;
        } else {
          _tmpUid = _cursor.getString(_cursorIndexOfUid);
        }
        _result.setUid(_tmpUid);
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        _result.setEmail(_tmpEmail);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _result.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _result.setUserType(_tmpUserType);
        final boolean _tmpIsProfileComplete;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsProfileComplete);
        _tmpIsProfileComplete = _tmp != 0;
        _result.setProfileComplete(_tmpIsProfileComplete);
        final boolean _tmpIsAvailable;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsAvailable);
        _tmpIsAvailable = _tmp_1 != 0;
        _result.setAvailable(_tmpIsAvailable);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _result.setFullName(_tmpFullName);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpProfilePicUrl;
        if (_cursor.isNull(_cursorIndexOfProfilePicUrl)) {
          _tmpProfilePicUrl = null;
        } else {
          _tmpProfilePicUrl = _cursor.getString(_cursorIndexOfProfilePicUrl);
        }
        _result.setProfilePicUrl(_tmpProfilePicUrl);
        final String _tmpAbout;
        if (_cursor.isNull(_cursorIndexOfAbout)) {
          _tmpAbout = null;
        } else {
          _tmpAbout = _cursor.getString(_cursorIndexOfAbout);
        }
        _result.setAbout(_tmpAbout);
        final int _tmpFamilySize;
        _tmpFamilySize = _cursor.getInt(_cursorIndexOfFamilySize);
        _result.setFamilySize(_tmpFamilySize);
        final int _tmpMinBudget;
        _tmpMinBudget = _cursor.getInt(_cursorIndexOfMinBudget);
        _result.setMinBudget(_tmpMinBudget);
        final int _tmpMaxBudget;
        _tmpMaxBudget = _cursor.getInt(_cursorIndexOfMaxBudget);
        _result.setMaxBudget(_tmpMaxBudget);
        final String _tmpServicesNeeded;
        if (_cursor.isNull(_cursorIndexOfServicesNeeded)) {
          _tmpServicesNeeded = null;
        } else {
          _tmpServicesNeeded = _cursor.getString(_cursorIndexOfServicesNeeded);
        }
        _result.setServicesNeeded(_tmpServicesNeeded);
        final int _tmpExperience;
        _tmpExperience = _cursor.getInt(_cursorIndexOfExperience);
        _result.setExperience(_tmpExperience);
        final int _tmpExpectedSalary;
        _tmpExpectedSalary = _cursor.getInt(_cursorIndexOfExpectedSalary);
        _result.setExpectedSalary(_tmpExpectedSalary);
        final String _tmpSkills;
        if (_cursor.isNull(_cursorIndexOfSkills)) {
          _tmpSkills = null;
        } else {
          _tmpSkills = _cursor.getString(_cursorIndexOfSkills);
        }
        _result.setSkills(_tmpSkills);
        final String _tmpLanguages;
        if (_cursor.isNull(_cursorIndexOfLanguages)) {
          _tmpLanguages = null;
        } else {
          _tmpLanguages = _cursor.getString(_cursorIndexOfLanguages);
        }
        _result.setLanguages(_tmpLanguages);
        final String _tmpAvailability;
        if (_cursor.isNull(_cursorIndexOfAvailability)) {
          _tmpAvailability = null;
        } else {
          _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
        }
        _result.setAvailability(_tmpAvailability);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<User> getAllEmployees() {
    final String _sql = "SELECT * FROM users WHERE userType = 'EMPLOYEE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "userType");
      final int _cursorIndexOfIsProfileComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "isProfileComplete");
      final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfProfilePicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "profilePicUrl");
      final int _cursorIndexOfAbout = CursorUtil.getColumnIndexOrThrow(_cursor, "about");
      final int _cursorIndexOfFamilySize = CursorUtil.getColumnIndexOrThrow(_cursor, "familySize");
      final int _cursorIndexOfMinBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "minBudget");
      final int _cursorIndexOfMaxBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "maxBudget");
      final int _cursorIndexOfServicesNeeded = CursorUtil.getColumnIndexOrThrow(_cursor, "servicesNeeded");
      final int _cursorIndexOfExperience = CursorUtil.getColumnIndexOrThrow(_cursor, "experience");
      final int _cursorIndexOfExpectedSalary = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedSalary");
      final int _cursorIndexOfSkills = CursorUtil.getColumnIndexOrThrow(_cursor, "skills");
      final int _cursorIndexOfLanguages = CursorUtil.getColumnIndexOrThrow(_cursor, "languages");
      final int _cursorIndexOfAvailability = CursorUtil.getColumnIndexOrThrow(_cursor, "availability");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUid;
        if (_cursor.isNull(_cursorIndexOfUid)) {
          _tmpUid = null;
        } else {
          _tmpUid = _cursor.getString(_cursorIndexOfUid);
        }
        _item.setUid(_tmpUid);
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        _item.setEmail(_tmpEmail);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _item.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _item.setUserType(_tmpUserType);
        final boolean _tmpIsProfileComplete;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsProfileComplete);
        _tmpIsProfileComplete = _tmp != 0;
        _item.setProfileComplete(_tmpIsProfileComplete);
        final boolean _tmpIsAvailable;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsAvailable);
        _tmpIsAvailable = _tmp_1 != 0;
        _item.setAvailable(_tmpIsAvailable);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _item.setFullName(_tmpFullName);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpProfilePicUrl;
        if (_cursor.isNull(_cursorIndexOfProfilePicUrl)) {
          _tmpProfilePicUrl = null;
        } else {
          _tmpProfilePicUrl = _cursor.getString(_cursorIndexOfProfilePicUrl);
        }
        _item.setProfilePicUrl(_tmpProfilePicUrl);
        final String _tmpAbout;
        if (_cursor.isNull(_cursorIndexOfAbout)) {
          _tmpAbout = null;
        } else {
          _tmpAbout = _cursor.getString(_cursorIndexOfAbout);
        }
        _item.setAbout(_tmpAbout);
        final int _tmpFamilySize;
        _tmpFamilySize = _cursor.getInt(_cursorIndexOfFamilySize);
        _item.setFamilySize(_tmpFamilySize);
        final int _tmpMinBudget;
        _tmpMinBudget = _cursor.getInt(_cursorIndexOfMinBudget);
        _item.setMinBudget(_tmpMinBudget);
        final int _tmpMaxBudget;
        _tmpMaxBudget = _cursor.getInt(_cursorIndexOfMaxBudget);
        _item.setMaxBudget(_tmpMaxBudget);
        final String _tmpServicesNeeded;
        if (_cursor.isNull(_cursorIndexOfServicesNeeded)) {
          _tmpServicesNeeded = null;
        } else {
          _tmpServicesNeeded = _cursor.getString(_cursorIndexOfServicesNeeded);
        }
        _item.setServicesNeeded(_tmpServicesNeeded);
        final int _tmpExperience;
        _tmpExperience = _cursor.getInt(_cursorIndexOfExperience);
        _item.setExperience(_tmpExperience);
        final int _tmpExpectedSalary;
        _tmpExpectedSalary = _cursor.getInt(_cursorIndexOfExpectedSalary);
        _item.setExpectedSalary(_tmpExpectedSalary);
        final String _tmpSkills;
        if (_cursor.isNull(_cursorIndexOfSkills)) {
          _tmpSkills = null;
        } else {
          _tmpSkills = _cursor.getString(_cursorIndexOfSkills);
        }
        _item.setSkills(_tmpSkills);
        final String _tmpLanguages;
        if (_cursor.isNull(_cursorIndexOfLanguages)) {
          _tmpLanguages = null;
        } else {
          _tmpLanguages = _cursor.getString(_cursorIndexOfLanguages);
        }
        _item.setLanguages(_tmpLanguages);
        final String _tmpAvailability;
        if (_cursor.isNull(_cursorIndexOfAvailability)) {
          _tmpAvailability = null;
        } else {
          _tmpAvailability = _cursor.getString(_cursorIndexOfAvailability);
        }
        _item.setAvailability(_tmpAvailability);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
