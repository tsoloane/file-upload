import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";

type SocketMessage = {
  file1: string,
  file2: string,
  rowCount: number,
  collationPolicy: string
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService extends EventTarget {
  ws!: WebSocket
  private processed: Event = new Event('processed');

  connect() {
    this.ws = new WebSocket(environment.webSocketUrl);
    this.ws.addEventListener('open', this.onOpen)
    this.ws.addEventListener('close', this.onClose)
    this.ws.addEventListener('error', this.onError)
    this.ws.binaryType = "blob"
  }

  send(file1: string, file2: string, rows: number, policy: string) : Promise<string> {
    if (!this.ws) this.connect();  //Connect if unconnected.
    return new Promise<string>((resolve, reject) => {
      let message = {
        file1: file1,
        file2: file2,
        rowCount: rows,
        collationPolicy: policy
      }
      this.ws.send(JSON.stringify(message))
      this.ws.onmessage = (ev => {
        let csv=ev.data
        resolve(csv)
      })
    })
  }

  //File processing completed - result from processing included.
  onFilesProcessed(event: MessageEvent) {
    let csv = event.data
    console.log("result from websocket\n"+csv)
    dispatchEvent(new CustomEvent("processed", {detail: csv}))
  }

  //Websocket event Listeners
  onOpen(event: Event) {
    console.log("Websocket is open, ready to receive")
  }

  onError(event: Event) {
    console.log("Websocket Error:  " + JSON.stringify(event.target) + " !!!")
  }

  onClose(event: Event) {
    console.log("Websocket closed!!!")
  }
}
