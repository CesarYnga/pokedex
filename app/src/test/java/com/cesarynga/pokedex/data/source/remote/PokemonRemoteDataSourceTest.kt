package com.cesarynga.pokedex.data.source.remote

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
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

        pokemonRemoteDataSource = PokemonRemoteDataSource(pokemonApi, Dispatchers.Main)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun `Given first page is requested, When no errors occurs, Then Pokemon list is received`() = runBlocking {
        val url = javaClass.classLoader!!.getResource("api-response/pokemon-list-first-page-response.json")
        val file = File(url.path)
        val json = String(file.readBytes())

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = pokemonApi.getPokemonList()

        assertThat(response.count).isEqualTo(POKEMON_LIST_COUNT)
        assertThat(response.next).isNotNull()
        assertThat(response.next).isEqualTo("https://pokeapi.co/api/v2/pokemon?offset=20&limit=20")
        assertThat(response.previous).isNull()
        assertThat(response.results.size).isEqualTo(POKEMON_LIST_SIZE)
    }

    @Test
    fun `Given middle page is requested, When no errors occurs, Then Pokemon list is received`() = runBlocking {
        val url = javaClass.classLoader!!.getResource("api-response/pokemon-list-middle-page-response.json")
        val file = File(url.path)
        val json = String(file.readBytes())

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = pokemonApi.getPokemonList()

        assertThat(response.count).isEqualTo(POKEMON_LIST_COUNT)
        assertThat(response.next).isNotNull()
        assertThat(response.next).isEqualTo("https://pokeapi.co/api/v2/pokemon?offset=100&limit=20")
        assertThat(response.previous).isNotNull()
        assertThat(response.previous).isEqualTo( "https://pokeapi.co/api/v2/pokemon?offset=60&limit=20")
        assertThat(response.results.size).isEqualTo(POKEMON_LIST_SIZE)
    }

    @Test
    fun `Given last page is requested, When no errors occurs, Then Pokemon list is received`() = runBlocking {
        val url = javaClass.classLoader!!.getResource("api-response/pokemon-list-last-page-response.json")
        val file = File(url.path)
        val json = String(file.readBytes())

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        val response = pokemonApi.getPokemonList()

        assertThat(response.count).isEqualTo(POKEMON_LIST_COUNT)
        assertThat(response.next).isNull()
        assertThat(response.previous).isNotNull()
        assertThat(response.previous).isEqualTo( "https://pokeapi.co/api/v2/pokemon?offset=1100&limit=20")
        assertThat(response.results.size).isEqualTo( 6)
    }

    companion object {
        private const val POKEMON_LIST_SIZE = 20
        private const val POKEMON_LIST_COUNT = 1126
    }
}