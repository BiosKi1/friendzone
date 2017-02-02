package com.example.junzi.friendzone;

/**
 * Created by Junzi on 31/01/2017.
 */

public class Config {

    //Address of our scripts of the CR
    public static final String URL_GET_ALL = "http://192.168.224.102/friendzoneapi/CRUD/api/api.php?fichier=users&action=users_liste";
    /*public static final String URL_GET_EMP = "http://192.168.224.102/friendzoneapi/CRUD/getEmp.php?id=";
    public static final String URL_UPDATE_EMP = "http://192.168.224.102/friendzoneapi/CRUD/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://192.168.224.102/friendzoneapi/CRUD/deleteEmp.php?id=";*/

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_PSEUDO = "pseudo";
    public static final String KEY_EMP_TELEPHONE = "tel";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "nom";
    public static final String TAG_PSEUDO = "pseudo";
    public static final String TAG_TELEPHONE = "tel";

    /*//employee id to pass with intent
    public static final String EMP_ID = "emp_id";*/
}
