package com.seadox.aquamanpro.Utilities;

public class ErrorsMsg {
    public class DB {
        public static final String USER_EXIST_MSG = "User Exist";
        public static final String VALID_USER_MSG = "The email or password is incorrect";
    }

    public class DB_Image {
        public static final String IMAGE_ERROR_MSG = "Fail to upload the image";
        public static final String IMAGE_SUCCESS_MSG = "The image has been uploaded successfully";
    }

    public class CreateWorkout {
        public static final String POSITIVE_NUMBERS = "Please insert positive numbers";
        public static final String INVALID_INPUT = "Invalid input";
        public static final String DISTANCE_ERROR = "Make sure the distance is in pools of 25/50";
        public static final String TIME_FORMAT_ERROR = "Make sure the time is in format 00:00 and max 60 min and 60 sec";
        public static final String LAPS_ERROR = "Too many laps";
    }
}
