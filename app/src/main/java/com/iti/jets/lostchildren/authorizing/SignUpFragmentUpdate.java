package com.iti.jets.lostchildren.authorizing;

import com.iti.jets.lostchildren.pojos.User;

/**
 * Created by Fadwa on 05/06/2018.
 */

public interface SignUpFragmentUpdate extends AuthFragmentsHome {

    void showDuplicatedEmailErrorMsg(boolean isDuplicated);

    void uploadUserImage(User newUser);

}
