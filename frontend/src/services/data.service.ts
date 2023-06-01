import { Injectable } from "@angular/core";
import { ResultBody, UpdateResultBody } from "src/schemas/schemas";

@Injectable({
  providedIn: "root",
})
export class DataService {
  async getUserResult(userId: string): Promise<ResultBody | undefined> {
    try {
      const response = await fetch(`http://localhost:8000/api/stats/${userId}`);

      const result = response.ok
        ? response.json().then((data) => JSON.parse(JSON.stringify(data)))
        : undefined;

      return result;
    } catch (error) {
      console.error("API-Error - getUserResult: ", error);

      return;
    }
  }

  postUserResult(body: ResultBody) {
    try {
      return fetch("http://localhost:8000/api/stats", {
        method: "POST",
        body: JSON.stringify(body),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      });
    } catch (error) {
      console.error("API-Error - postUserResult: ", error);

      return;
    }
  }

  updateUserResult(body: UpdateResultBody, userId: string) {
    try {
      return fetch(`http://localhost:8000/api/stats/${userId}`, {
        method: "PUT",
        body: JSON.stringify(body),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      });
    } catch (error) {
      console.error("API-Error - updateUserResult: ", error);

      return;
    }
  }
}
