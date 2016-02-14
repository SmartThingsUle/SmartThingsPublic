/**
 *  OSRAM Lightify Dimming Switch
 *
 *  Copyright 2016 Michael Hudson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Thanks to @Sticks18 for the Hue Dimmer remote code used as a base for this!
 *
 */
metadata {
  definition (name: "OSRAM Lightify Dimming Switch", namespace: "motley74", author: "Michael Hudson") {
    capability "Actuator"
    capability "Button"
    capability "Configuration"
    capability "Refresh"
    capability "Sensor"
    //capability "Switch"
    //capability "Switch Level"

    fingerprint profileId: "0104", deviceId: "0001", inClusters: "0000, 0001, 0003, 0020, 0402, 0B05", outClusters: "0003, 0006, 0008, 0019" //, manufacturer: "OSRAM", model: "Lightify 2.4GHZZB/SWITCH/LFY", deviceJoinName: "OSRAM Lightify Dimming Switch"
  }

  simulator {
    // TODO: define status and reply messages here
  }

  tiles {
    tiles {
      standardTile("button", "device.button", width: 2, height: 2) {
        state "default", label: "", icon: "st.unknown.zwave.remote-controller", backgroundColor: "#ffffff"
      }
      main "button"
      details(["button"])
    }
  }
}

def parse(String description) {
  log.debug "parse description: $description"
    
  // Create a map from the raw zigbee message to make parsing more intuitive
  def msg = zigbee.parse(description)
  log.debug msg
  
  // Call proper method based on command received
  log.debug "Command is $msg.command and data is $msg.data"
  if ((msg.command==0) || (msg.command==1 && !msg.data)) {
    lightPower(msg.command)
  } else if ((msg.command==1 && msg.data) || (msg.command==5 || msg.command==3)) {
    lightLevel(msg.command, msg.data)
  } else {
    log.warn "Unknown command/data sequence received! Command: $msg.command Data: $msg.data"
  }
}

def configure() {
  log.debug "Executing 'configure'"

  def configCmds = [	
    // Bind the outgoing on/off cluster from remote to hub, so remote sends hub messages when On/Off buttons pushed
    "zdo bind 0x${device.deviceNetworkId} 0x01 0x01 0x0006 {${device.zigbeeId}} {}",

    // Bind the outgoing level cluster from remote to hub, so remote sends hub messages when Dim Up/Down buttons pushed
    "zdo bind 0x${device.deviceNetworkId} 0x01 0x01 0x0008 {${device.zigbeeId}} {}",
  ]
  return configCmds
}

def lightPower(command) {
  if (command==0) {
    log.debug "Turning light(s) off."
    //TODO: Create power off event
  } else {
    log.debug "Turning light(s) on."
    //TODO: Create power on event
  }
}

def lightLevel(command, data) {
  if (command==1) {
    log.debug "Increasing light(s) brightness."
    //TODO: Create light level decrease action 
  } else if (command==5) {
    log.debug "Decreasing light(s) brightness."
    //TODO: Create light level increase action
  } else {
    log.debug "Stopping brightness change."
    //TODO: Create light level stop action
  }
}