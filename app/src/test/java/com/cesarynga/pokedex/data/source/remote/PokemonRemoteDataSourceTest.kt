package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@ExperimentalCoroutinesApi
class PokemonRemoteDataSourceTest {

    private lateinit var pokemonRemoteDataSource: PokemonRemoteDataSource
    private lateinit var pokemonApi: PokemonApi
    private lateinit var mockServer: MockWebServer

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        pokemonApi = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)

        pokemonRemoteDataSource = PokemonRemoteDataSourceImpl(pokemonApi, Dispatchers.Main)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun `Given server available, when first page is requested, then pokemon list is received`() = runTest {
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
    fun `Given server available, when middle page is requested, then pokemon list is received`() = runTest {
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
    fun `Given server available, when last page is requested, then pokemon list is received`() = runTest {
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

    @Test
    fun `Given server unavailable, when a page is requested, then error is returned`() = runTest {
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(404)
        )
        try {
            pokemonApi.getPokemonList()
        } catch (e: Exception) {
            assertThat(e).isNotNull()
            assertThat(e).isInstanceOf(HttpException::class.java)
        }
    }

    companion object {
        private const val POKEMON_LIST_SIZE = 20
        private const val POKEMON_LIST_COUNT = 1126
    }
}