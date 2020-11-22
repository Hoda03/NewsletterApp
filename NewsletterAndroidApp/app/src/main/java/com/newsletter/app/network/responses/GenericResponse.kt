package com.newsletter.app.network.responses

import com.newsletter.app.network.enums.Endpoint
import com.newsletter.app.network.enums.Status

class GenericResponse(
    val status: Status,
    val data: Any?,
    val error: Throwable?,
    val endpoint: Endpoint
) {

    companion object {
        fun loading(endpoint: Endpoint): GenericResponse? {
            return GenericResponse(Status.LOADING, null, null, endpoint)
        }

        fun success(data: Any?, endpoint: Endpoint): GenericResponse? {
            return GenericResponse(Status.SUCCESS, data, null, endpoint)
        }

        fun error(error: Throwable?, endpoint: Endpoint): GenericResponse? {
            return GenericResponse(Status.ERROR, null, error, endpoint)
        }

    }
}
