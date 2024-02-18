package com.wolttrainee.deliveryfeecalculator

import com.fasterxml.jackson.databind.ObjectMapper
import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeRequest
import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeResponse
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class DeliveryFeeCalculatorApplicationTests {

	// {"cart_value": 790, "delivery_distance": 2235, "number_of_items": 4, "time": "2024-01-15T13:00:00Z"}
	// {"delivery_fee": 710}

	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var objectMapper: ObjectMapper

	val baseUrl: String = "/api/fees/delivery-fee"

	// {"cart_value": 20100, "delivery_distance": 2235, "number_of_items": 4, "time": "2024-01-15T13:00:00Z"}
	@Test
	fun `cart value above 200,  should return 0`() {

		val deliveryFeeRequest = DeliveryFeeRequest(20100, 1000, 4, "2024-01-27T13:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(0)))
				}
			}
	}

	// {"cart_value": 19900, "delivery_distance": 1000, "number_of_items": 4, "time": "2024-01-27T13:00:00Z"}
	@Test
	fun `base case - no surcharges`() {

		val deliveryFeeRequest = DeliveryFeeRequest(19900, 1000, 4, "2024-01-27T13:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(200)))
				}
			}
	}

	// {"cart_value": 19900, "delivery_distance": 1400, "number_of_items": 4, "time": "2024-01-27T13:00:00Z"}
	@Test
	fun `base case + 1400 distance`() {

		val deliveryFeeRequest = DeliveryFeeRequest(19900, 1400, 4, "2024-01-27T13:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(300)))
				}
			}
	}

	// {"cart_value": 19900, "delivery_distance": 1400, "number_of_items": 4, "time": "2024-01-27T13:00:00Z"}
	@Test
	fun `base case + no of items is 5`() {

		val deliveryFeeRequest = DeliveryFeeRequest(19900, 1000, 5, "2024-01-27T13:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(250)))
				}
			}
	}

	// {"cart_value": 19900, "delivery_distance": 1000, "number_of_items": 4, "time": "2024-01-26T15:00:00Z"}
	@Test
	fun `base case + rush hour`() {

		val deliveryFeeRequest = DeliveryFeeRequest(19900, 1000, 4, "2024-01-26T15:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(240)))
				}
			}
	}

	// {"cart_value": 900, "delivery_distance": 1000, "number_of_items": 4, "time": "2024-01-26T15:00:00Z"}
	@Test
	fun `cart value less than 10 eur`() {

		val deliveryFeeRequest = DeliveryFeeRequest(900, 1000, 4, "2024-01-27T15:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(300)))
				}
			}
	}

	// {"cart_value": 900, "delivery_distance": 1501, "number_of_items": 6, "time": "2024-01-26T15:00:00Z"}
	//"cart_value": 900 = (100)
	//"delivery_distance": 1501 = 200 + 100 + 100 = (400)
	//"number_of_items": 6 = 50 + 50 = (100)
	// rush hour = fee * 1.2
	//total expected fee = (100 + 400 + 100 )* 1.2 = 720
	@Test
	fun `complex scenario - multiple variables`() {

		val deliveryFeeRequest = DeliveryFeeRequest(900, 1501, 6, "2024-01-26T15:00:00Z")

		val performPost = mockMvc.post(baseUrl) {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(deliveryFeeRequest)
		}

		performPost
			.andDo { print() }
			.andExpect {
				content {
					json(objectMapper.writeValueAsString(DeliveryFeeResponse(720)))
				}
			}
	}

}
