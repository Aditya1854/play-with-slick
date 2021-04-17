package utilities
import pdi.jwt.{ Jwt, JwtAlgorithm, JwtClaim , JwtHeader}
class JwtUtility {
  val secretKey = "alwaysBeASecret"
  val JwtSecretAlgo = "HS256"

  def createToken(payload: String): String = {
    val header = JwtHeader(JwtSecretAlgo)
    val claim = JwtClaim(payload)
    Jwt.encode(claim, secretKey, JwtSecretAlgo )
  }

  def isValidToken(jwtToken: String): Boolean =
    JsonWebToken.validate(jwtToken, JwtSecretKey)

  def decodePayload(jwtToken: String): Option[String] =
    jwtToken match {
      case JsonWebToken(header, claimsSet, signature) => Option(claimsSet.asJsonString)
      case _ => None
    }
}

object JwtUtility extends JwtUtility
