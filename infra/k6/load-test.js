import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  stages: [
    { duration: "30s", target: 20 },
    { duration: "1m", target: 50 },
    { duration: "30s", target: 0 }
  ],
  thresholds: {
    http_req_duration: ["p(95)<750"],
    http_req_failed: ["rate<0.02"]
  }
};

const baseUrl = __ENV.API_BASE_URL || "http://localhost:8080";

export default function () {
  const health = http.get(`${baseUrl}/actuator/health`);
  check(health, { "health is up": (res) => res.status === 200 });
  sleep(1);
}
