package com.hcdisat.dataaccess.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

fun interface GoogleCredentialsProvider {
    fun getCredentials(tokenId: String): AuthCredential
}

class GoogleCredentialsProviderImpl @Inject constructor() : GoogleCredentialsProvider {
    override fun getCredentials(tokenId: String): AuthCredential =
        GoogleAuthProvider.getCredential(tokenId, null)
}