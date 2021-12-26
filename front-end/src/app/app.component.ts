import {Component, OnInit} from '@angular/core';
import {WebSocketService} from "./service/web-socket.service";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FileChangeEvent} from "@angular/compiler-cli/src/perform_watch";
import {saveAs} from "file-saver";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Process CSV Files';
  filesForm!: FormGroup;
  file1!: File;
  file1Contents!: string
  file2!: File;
  file2Contents!:string
  rows: number=5;
  download: boolean = true;
  collationPolicy: string = "FULL";
  result: string = "";

  constructor(private socketService: WebSocketService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.filesForm = this.fb.group({
      file1: ['', Validators.required],
      file2: ['', Validators.required],
      rows: ['', [Validators.required, Validators.min(5), Validators.max(500)]],
      download: [''],
      collationPolicy: ['']
    });
    this.socketService.addEventListener("processed",(e)=>{
      this.result=(<CustomEvent>e).detail
      console.log("result updated to.\n"+this.result)
    })
    this.socketService.connect()
  }

  // @ts-ignore
  file1Changed(evt): void {
    console.log("File1 uploaded")
    if (evt.target && evt.target.files[0]) {
      let file = evt.target.files[0];
      let reader = new FileReader();
      reader.onload = (e)=>{
        this.file1Contents =reader.result as string
      }
      reader.onloadend = (e) => {
        console.log("File Load complete")
      }
      reader.readAsText(file)
    }
  }

  // @ts-ignore
  file2Changed(evt): void {
    console.log("File2 uploaded")
    if (evt.target && evt.target.files[0]) {
      let file = evt.target.files[0];
      let reader = new FileReader();
      reader.onload = (e)=>{
        this.file2Contents =reader.result as string
      }
      reader.onloadend = (e) => {
        console.log("File Load complete")
      }
      reader.readAsText(file)
    }
  }

  process(): void {
    console.log("Processing files...")
    this.socketService.send(this.file1Contents, this.file2Contents, this.rows, this.collationPolicy)
      .then(data=> {
        if(this.download) {
          const blob = new Blob([data])
          saveAs(blob, "processed_file.csv")
        }
        else {
            this.result = data
          }
        }
      );
  }
}
