package com.burov.movie.discovery.app.domain

interface UseCase<in Params, out Type> where Type : Any {
    suspend fun execute(params: Params): Type
}