import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule } from "@angular/common/http";
import { AppComponent } from "./app.component";
import {
  HeaderComponent,
  HandGestureComponent,
  RockPaperScissorComponent,
} from "../components";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ToastrModule } from "ngx-toastr";
import { CommonModule } from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    HandGestureComponent,
    HeaderComponent,
    RockPaperScissorComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    CommonModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
