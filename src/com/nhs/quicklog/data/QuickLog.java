package com.nhs.quicklog.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class QuickLog {
    public static final String AUTHORITY = "com.nhs.quicklog.provider.Procedure";

    private QuickLog() {
    }

    public static final class Procedures implements BaseColumns {

        private Procedures() {}

        public static final String TABLE_NAME = "procedures";

        private static final String SCHEME = "content://";

        private static final String PATH_PROCEDURES = "/procedures";

        private static final String PATH_PROCEDURE_MATCH = "/procedures/";

        public static final int PROCEDURE_ID_PATH_POSITION = 1;

        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_PROCEDURES);

        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_PROCEDURE_MATCH);

		public static final Uri CONTENT_ID_URI_PATTERN
            = Uri.parse(SCHEME + AUTHORITY + PATH_PROCEDURE_MATCH + "/#");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quicklog.procedure";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quicklog.procedure";

        public static final String DEFAULT_SORT_ORDER = "hits DESC, created DESC";

        public static final String COLUMN_NAME_NAME = "name";

		public static final String COLUMN_NAME_CREATE_DATE = "created";
		
		public static final String COLUMN_NAME_HITS = "hits";
		
		  public static final String[] PROJECTION = new String[] {
			  QuickLog.Procedures._ID,
			  QuickLog.Procedures.COLUMN_NAME_NAME,
			  QuickLog.Procedures.COLUMN_NAME_HITS,
	  };

	  public static final int COLUMN_INDEX_ID = 0;
	  public static final int COLUMN_INDEX_TITLE = 1;
	  public static final int COLUMN_INDEX_HITS = 2;
    }
}
