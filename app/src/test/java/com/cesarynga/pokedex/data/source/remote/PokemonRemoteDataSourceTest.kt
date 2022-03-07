package com.cesarynga.pokedex.data.source.remote

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class PokemonRemoteDataSourceTest {

    private lateinit var pokemonRemoteDataSource: PokemonRemoteDataSource
    private lateinit var pokemonApi: PokemonApi
    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        pokemonApi = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)

        pokemonRemoteDataSource = PokemonRemoteDataSource(pokemonApi)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun pokemonList_fetchFirstPage_returnSuccess() = runBlocking {
        val url = javaClass.classLoader!!.getResource("api-response/pokemon-list-first-page-response.json")
        val file = File(url.path)
        val json = String(file.readBytes())

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = pokemonApi.pokemonList()

        assertEquals(response.count, POKEMON_LIST_COUNT)
        assertNotNull(response.next)
        assertEquals(response.next, "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20")
        assertNull(response.previous)
        assertEquals(response.results.size, POKEMON_LIST_SIZE)
    }

    @Test
    fun pokemonList_fetchMiddlePage_returnSuccess() = runBlocking {
        val url = javaClass.classLoader!!.getResource("api-response/pokemon-list-middle-page-response.json")
        val file = File(url.path)
        val json = String(file.readBytes())

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = pokemonApi.pokemonList()

        assertEquals(response.count, POKEMON_LIST_COUNT)
        assertNotNull(response.next)
        assertEquals(response.next, "https://pokeapi.co/api/v2/pokemon?offset=100&limit=20")
        assertNotNull(response.previous)
        assertEquals(response.previous, "https://pokeapi.co/api/v2/pokemon?offset=60&limit=20")
        assertEquals(response.results.size, POKEMON_LIST_SIZE)
    }

    @Test
    fun pokemonList_fetchLastPage_returnSuccess() = runBlocking {
        val url = javaClass.classLoader!!.getResource("api-response/pokemon-list-last-page-response.json")
        val file = File(url.path)
        val json = String(file.readBytes())

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = pokemonApi.pokemonList()

        assertEquals(response.count, POKEMON_LIST_COUNT)
        assertNull(response.next)
        assertNotNull(response.previous)
        assertEquals(response.previous, "https://pokeapi.co/api/v2/pokemon?offset=1100&limit=20")
        assertEquals(response.results.size, 6)
    }

    companion object {
        private const val POKEMON_LIST_SIZE = 20
        private const val POKEMON_LIST_COUNT = 1126
    }
}