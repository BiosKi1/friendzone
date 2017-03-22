package com.example.junzi.friendzone;

/**
 * Created by Junzi on 31/01/2017.
 */

public class Config {

    //public static String url = "http://192.168.224.112/friendzoneapi/api/";
    public static String url = "http://friendzone01.esy.es/php/friendzoneapi/api/";

    public static String id_user_co = "";
    //Address of our scripts of the CR
    public static final String URL_GET_ALL = url+"api.php?fichier=users&action=users_liste";

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
    public static final String TAG_NAME_AMI = "nom_ami";
    public static final String TAG_PRENOM_AMI = "prenom_ami";
    public static final String TAG_ID_AMI = "id_ami";
    public static final String TAG_PSEUDO = "pseudo";
    public static final String TAG_TELEPHONE = "tel";
    public static final String TAG_LONG_USER = "long_user";
    public static final String TAG_LAT_USER = "lat_user";
    public static final String TAG_LONG_AMI = "long_ami";
    public static final String TAG_LAT_AMI = "lat_ami";
    public static final String TAG_MAIL = "mail";
    public static final String TAG_FIRST_NAME_USER = "prenom";

    public static final String TAG_ADRESSE_LIEU = "adresse";
    public static final String TAG_ADRESSE_NOM = "libelle";
    public static final String TAG_LAT_LIEU = "lat";
    public static final String TAG_LONG_LIEU = "longi";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";
}
