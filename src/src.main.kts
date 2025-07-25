@file:Repository("https://download.jetbrains.com/teamcity-repository/")
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.jetbrains.teamcity:serviceMessages:2024.12")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
@file:CompilerOptions("-opt-in=kotlin.RequiresOptIn")

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import jetbrains.buildServer.messages.serviceMessages.ServiceMessage
import jetbrains.buildServer.messages.serviceMessages.ServiceMessageTypes.BUILD_SET_PARAMETER
import kotlinx.serialization.json.JsonArray

val githubAPIVersion: String = "2022-11-28"

val repository: String = System.getenv("input_repository") ?: error("Input \"repository\" is not set.")
val ref: String = System.getenv("input_ref") ?: error("Input \"ref\" is not set.")
val accessToken: String? = System.getenv("input_github_access_token")
val variableName: String = System.getenv("input_variable_name") ?: error("Input \"variable_name\" is not set.")

val client: HttpClient = HttpClient.newBuilder().build()
val requestBuilder: HttpRequest.Builder = HttpRequest.newBuilder()
	.uri(URI.create("https://api.github.com/repos/$repository/git/matching-refs/$ref"))
	.header("Accept", "application/vnd.github+json")
	.header("X-GitHub-Api-Version", githubAPIVersion)
	.GET()

if (!accessToken.isNullOrBlank()) requestBuilder.header("Authorization", "Bearer $accessToken")

val request: HttpRequest = requestBuilder.build()
val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
val statusCode: Int = response.statusCode()
val responseBody: String = response.body()

when (statusCode) {
	200 -> {
		val element = Json.parseToJsonElement(responseBody)
		if (element !is JsonArray) error("Expected JSON array but got: ${element::class.simpleName}")
		if (element.isEmpty()) error("No matching refs found for pattern: $ref")

		val latestRef = element.last()
		val refName = latestRef.jsonObject["ref"]?.jsonPrimitive?.content
			?: error("Could not extract ref name from response")
		val cleanRefName = refName.substringAfter("refs/").substringAfter("/")

		val message = ServiceMessage.asString(
			BUILD_SET_PARAMETER,
			mapOf(
				"name" to "env.$variableName",
				"value" to cleanRefName
			)
		)
		println(message)
	}
	else -> error("Failed to fetch latest matching ref (HTTP $statusCode): $responseBody")
}
