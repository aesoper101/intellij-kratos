package service

import (
	"context"
	"github.com/aesoper101/kratos-utils/pkg/middleware/translator"

	v1 "github.com/aesoper101/kratos-layout/api/helloworld/v1"
	"github.com/aesoper101/kratos-layout/internal/biz"
)

// GreeterService is a greeter service.
type GreeterService struct {
	v1.UnimplementedGreeterServer

	uc *biz.GreeterLogic
}

// NewGreeterService new a greeter service.
func NewGreeterService(uc *biz.GreeterLogic) *GreeterService {
	return &GreeterService{uc: uc}
}

// SayHello implements helloworld.GreeterServer.
func (s *GreeterService) SayHello(ctx context.Context, in *v1.HelloRequest) (*v1.HelloReply, error) {
	g, err := s.uc.CreateGreeter(ctx, &biz.Greeter{Hello: in.Name})
	if err != nil {
		return nil, err
	}

	t := translator.FromTranslatorContext(ctx)
	return &v1.HelloReply{Message: "Hello " + g.Hello + "," + t.T("days-left", "10")}, nil
}
