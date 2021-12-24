import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";

const enum OutgoingMessageType {
  FILE1             = "FILE_1",
  FILE2             = "FILE_2",
  PROCESS_VARS      = "PROCESS_VARS",
  EXECUTE_PROCESS   = "EXECUTE_PROCESS"
}

const enum IncomingMessageType {
  FILE1_SIZE          = "FILE1_SIZE",
  FILE2_SIZE          = "FILE2_SIZE",
  PROCESS_RESULT      = "PROCESS_RESULT"
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  ws!: WebSocket

  connect() {
    this.ws = new WebSocket(environment.webSocketUrl);
    this.ws.addEventListener('open', this.onOpen)
    this.ws.addEventListener('close', this.onClose)
    this.ws.addEventListener('error', this.onError)
    this.ws.addEventListener('message', this.onFilesProcessed)
    this.ws.binaryType = "blob"
  }

  send(file1: string, file2: string, rows: number, policy: string) {
    alert("sending files, ws="+this.ws)
    alert("file1:\n"+file1)
    alert("file2:\n"+file2)
    if(!this.ws) this.connect();  //Connect if unconnected.
    let message = {
      file1: file1,
      file2: file2,
      rowCount: rows,
      collationPolicy:  policy
    }
    this.ws.send(JSON.stringify(message))
  }

  //File processing completed - result from processing included.
  onFilesProcessed(event: MessageEvent) {
  }

  //Websocket event Listeners
  onOpen(event: Event) {
    console.log("Websocket is open")
  }

  onError(event: Event) {
    console.log("Websocket Error:  "+ event.target+" !!!")
  }

  onClose(event: Event) {
    console.log("Websocket closed!!!")
  }
}
