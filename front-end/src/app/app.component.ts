import {Component, OnInit} from '@angular/core';
import {WebSocketService} from "./service/web-socket.service";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FileChangeEvent} from "@angular/compiler-cli/src/perform_watch";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Process CSV Files';
  filesForm!: FormGroup;
  file1!: string;
  file2!: string;
  rows!: number;
  download: boolean = true;
  collationPolicy: string = "full";

  constructor(private socketService: WebSocketService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.filesForm = this.fb.group({
      file1: ['', Validators.required],
      file2: ['', Validators.required],
      rows: ['', [Validators.min(5), Validators.max(500)]],
      download: [''],
      collationPolicy: ['']
    });
  }

  //Read the file
  // @ts-ignore
  fileChanged(evt, content: string): void {
    new Promise<string>(resolve => {
      if (evt.target && evt.target.files[0]) {
        let file = evt.target.files[0];
        if (file) {
          let fr = new FileReader();
          fr.onload = (e)=> {
            let txt = fr.result;
            if(txt) {
              resolve(txt.toString())
            } else {
              resolve('')
            }
          }
          fr.readAsText(file);
        } else {
          resolve('')
        }
      }
      }).then(x =>content=x)
    }

    process():void {
    alert("Processing files...")
      this.socketService.send(this.file1, this.file2, this.rows, this.collationPolicy);
    }
  }
