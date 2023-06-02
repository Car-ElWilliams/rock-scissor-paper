import { Injectable } from "@angular/core";
import {
  PostResultBody,
  ResultBody,
  UpdateResultBody,
} from "src/schemas/schemas";

@Injectable({
  providedIn: "root",
})
export class DataService {
  async isConnectedToServer(): Promise<boolean> {
    try {
      const response = await fetch(`http://localhost:8000/api/stats`);

      return response.ok;
    } catch (error) {
      console.error("Not connected to server");
      return false;
    }
  }

  postNewUser(body: PostResultBody) {
    try {
      return fetch("http://localhost:8000/api/stats", {
        method: "POST",
        body: JSON.stringify(body),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      });
    } catch (error) {
      console.error("API-Error - postNewUser: ", error);

      return;
    }
  }

  async getUserResult(userId: string): Promise<ResultBody | undefined> {
    try {
      const response = await fetch(`http://localhost:8000/api/stats/${userId}`);

      const result = response.ok
        ? response.json().then((data) => data)
        : undefined;

      return result;
    } catch (error) {
      console.error("API-Error - getUserResult: ", error);

      return;
    }
  }

  async updateUserResult(body: UpdateResultBody, userId: string) {
    try {
      const result = await fetch(`http://localhost:8000/api/stats/${userId}`, {
        method: "PUT",
        body: JSON.stringify(body),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      });

      return result;
    } catch (error) {
      console.error("API-Error - updateUserResult: ", error);

      return;
    }
  }
}
