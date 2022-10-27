//go:build wireinject
// +build wireinject

// The build tag makes sure the stub is not built in the final build.

package main

import (
	"github.com/aeoper101/kratos-layout/internal/biz"
	"github.com/aeoper101/kratos-layout/internal/conf"
	"github.com/aeoper101/kratos-layout/internal/data"
	"github.com/aeoper101/kratos-layout/internal/i18n"
	"github.com/aeoper101/kratos-layout/internal/server"
	"github.com/aeoper101/kratos-layout/internal/service"
	"github.com/aesoper101/kratos-utils/bootstrap"
	"github.com/google/wire"
)

// wireApp init kratos application.
func wireApp(cfg bootstrap.ConfigFlags, srvInfo *bootstrap.ServiceInfo) (*bootstrap.App, func(), error) {
	panic(wire.Build(
		server.ProviderSet,
		data.ProviderSet,
		biz.ProviderSet,
		service.ProviderSet,
		i18n.ProviderSet,
		conf.ProviderSet,
		bootstrap.ProviderSet,
	))
}
