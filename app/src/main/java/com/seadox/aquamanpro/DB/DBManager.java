package com.seadox.aquamanpro.DB;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.seadox.aquamanpro.Interface.AuthenticationCallBack;
import com.seadox.aquamanpro.Interface.DBCallBack;
import com.seadox.aquamanpro.Models.DrillList;
import com.seadox.aquamanpro.Utilities.ErrorsMsg;
import com.seadox.aquamanpro.Utilities.SignalGenerator;

import java.util.ArrayList;

public class DBManager {
    private static final String TAG = "DBManager";
    //Reference
    private static final String WORKOUT_REF = "workouts";
    private static final String IMAGES_REF = "images/";

    //ORDER
    private static final String ORDER_DISTANCE = "distance";
    private static final String ORDER_UID = "uid";

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void getWorkoutsByDistanceFromDB(float down, float up, DBCallBack callBack) {
        ArrayList<DrillList> list = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference workoutsRef = db.getReference(WORKOUT_REF);

        workoutsRef.orderByChild(ORDER_DISTANCE)
                .startAt(down)
                .endAt(up)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            DrillList drillList = child.getValue(DrillList.class);
                            list.add(drillList);
                        }
                        callBack.initAdapter(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public static void getWorkoutsByUidFromDB(DBCallBack callBack) {
        ArrayList<DrillList> list = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference workoutsRef = db.getReference(WORKOUT_REF);
        String uid = getUser().getUid();

        workoutsRef.orderByChild(ORDER_UID)
                .equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            DrillList drillList = child.getValue(DrillList.class);
                            list.add(drillList);
                        }
                        callBack.initAdapter(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static void addWorkout(DrillList drillList) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(WORKOUT_REF);
        ref.push().setValue(drillList);
    }

    public static void signInUser(Activity activity, String email, String password, AuthenticationCallBack callBack) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            callBack.createSuccess();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            callBack.createFailed(ErrorsMsg.DB.VALID_USER_MSG);
                        }
                    }
                });
    }

    public static void createUser(Activity activity, String email, String password, String name, AuthenticationCallBack callBack) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            DBManager.updateUserDetails(DBManager.getUser().getPhotoUrl(), name, null, null);
                            callBack.createSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            callBack.createFailed(ErrorsMsg.DB.USER_EXIST_MSG);
                        }
                    }
                });
    }

    public static void uploadImage(Uri imageURi, DBCallBack callBack) {
        String fileName = String.valueOf(System.currentTimeMillis());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(IMAGES_REF + fileName);
        storageReference.putFile(imageURi).continueWithTask(task -> {
            if (!task.isSuccessful())
                SignalGenerator.getInstance().toast(ErrorsMsg.DB_Image.IMAGE_ERROR_MSG, Toast.LENGTH_SHORT);

            return storageReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadURi = task.getResult();
                updateUserDetails(downloadURi, getUser().getDisplayName(), ErrorsMsg.DB_Image.IMAGE_SUCCESS_MSG, callBack);
            }
        });
    }

    public static void updateUserDetails(Uri imageURi, String name, String toastMSG, DBCallBack callBack) {
        FirebaseUser user = getUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(imageURi)
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (toastMSG != null)
                            SignalGenerator.getInstance().toast(toastMSG, Toast.LENGTH_SHORT);

                        if (callBack != null)
                            callBack.saveImageToUser(user.getPhotoUrl());
                    }
                });
    }
}
