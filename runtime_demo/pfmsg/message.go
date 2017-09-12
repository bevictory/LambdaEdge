// Code generated by protoc-gen-go.
// source: message.proto
// DO NOT EDIT!

package pfmsg

import (
	proto "github.com/golang/protobuf/proto"
	"encoding/json"
	"math"
)


// Reference proto, json, and math imports to suppress error if they are not otherwise used.
var _ = proto.Marshal
var _ = &json.SyntaxError{}
var _ = math.Inf

type ClientMessage struct {
	DeviceId         *string `protobuf:"bytes,1,req,name=deviceId" json:"deviceId,omitempty"`
	Type             *int32  `protobuf:"varint,2,req,name=type" json:"type,omitempty"`
	Content          *string `protobuf:"bytes,3,req,name=content" json:"content,omitempty"`
	MessageId        *string `protobuf:"bytes,4,req,name=messageId" json:"messageId,omitempty"`
	CallbackId       *string `protobuf:"bytes,5,opt,name=callbackId" json:"callbackId,omitempty"`
	XXX_unrecognized []byte  `json:"-"`
}

func (m *ClientMessage) Reset()         { *m = ClientMessage{} }
func (m *ClientMessage) String() string { return proto.CompactTextString(m) }
func (*ClientMessage) ProtoMessage()    {}

func (m *ClientMessage) GetDeviceId() string {
	if m != nil && m.DeviceId != nil {
		return *m.DeviceId
	}
	return ""
}

func (m *ClientMessage) GetType() int32 {
	if m != nil && m.Type != nil {
		return *m.Type
	}
	return 0
}

func (m *ClientMessage) GetContent() string {
	if m != nil && m.Content != nil {
		return *m.Content
	}
	return ""
}

func (m *ClientMessage) GetMessageId() string {
	if m != nil && m.MessageId != nil {
		return *m.MessageId
	}
	return ""
}

func (m *ClientMessage) GetCallbackId() string {
	if m != nil && m.CallbackId != nil {
		return *m.CallbackId
	}
	return ""
}

type ServiceMessage struct {
	DeviceId         *string `protobuf:"bytes,1,req,name=deviceId" json:"deviceId,omitempty"`
	Type             *int32  `protobuf:"varint,2,req,name=type" json:"type,omitempty"`
	Content          *string `protobuf:"bytes,3,req,name=content" json:"content,omitempty"`
	MessageId        *string `protobuf:"bytes,4,req,name=messageId" json:"messageId,omitempty"`
	XXX_unrecognized []byte  `json:"-"`
}

func (m *ServiceMessage) Reset()         { *m = ServiceMessage{} }
func (m *ServiceMessage) String() string { return proto.CompactTextString(m) }
func (*ServiceMessage) ProtoMessage()    {}

func (m *ServiceMessage) GetDeviceId() string {
	if m != nil && m.DeviceId != nil {
		return *m.DeviceId
	}
	return ""
}

func (m *ServiceMessage) GetType() int32 {
	if m != nil && m.Type != nil {
		return *m.Type
	}
	return 0
}

func (m *ServiceMessage) GetContent() string {
	if m != nil && m.Content != nil {
		return *m.Content
	}
	return ""
}

func (m *ServiceMessage) GetMessageId() string {
	if m != nil && m.MessageId != nil {
		return *m.MessageId
	}
	return ""
}

func init() {
}
