package com.example.junzi.friendzone;

/**
 * Created by Junzi on 31/01/2017.
 */

public class Config {
    public static String ip = "10.5.199.172";
    public static String id_user_co = "";
    //Address of our scripts of the CR
    public static final String URL_GET_ALL = "http://"+ip+"/projet/friendzoneapi/api/api.php?fichier=users&action=users_liste";
    public static final String URL_CONNECT= "http://"+ip+"/projet/friendzoneapi/api/api.php/?fichier=users&action=connexion&values[mail]=jaudstin2@usnews.com&values[mdp]=gnGnAZS9z";
    public static final String URL_GET_EMP = "http://"+ip+"/projet/friendzoneapi/getEmp.php?id=";
    public static final String URL_UPDATE_EMP = "http://"+ip+"/projet/friendzoneapi/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://"+ip+"/projet/friendzoneapi/deleteEmp.php?id=";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_ID_USER = "id_user";
    public static final String KEY_EMP_ID_AMI= "id_ami";

    public static final String KEY_EMP_NAME = "nom";
    public static final String KEY_EMP_PSEUDO = "pseudo";
    public static final String KEY_EMP_TELEPHONE = "tel";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME_USER = "nom_user";
    public static final String TAG_ID_USER = "id_user";
    public static final String TAG_ID_AMI = "id_ami";
    public static final String TAG_PSEUDO = "pseudo";
    public static final String TAG_TELEPHONE = "tel";
    public static final String TAG_LONG_USER = "long_user";
    public static final String TAG_LAT_USER = "lat_user";
    public static final String TAG_MAIL = "mail";
    public static final String TAG_FIRST_NAME_USER = "prenom";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";
}
