package helper

import (
	"context"

	"github.com/99designs/gqlgen/graphql"
)

func NormalizeQueryResult(data interface{}) interface{} {
	return data.([]interface{})[0].(map[string]interface{})["result"]
}
func NormalizeFields(ctx context.Context) string {
	arrFields := graphql.CollectAllFields(ctx)
	fields := ""
	for i, field := range arrFields {
		fields += field
		if i < len(arrFields)-1 {
			fields += ","
		}
	}
	return fields
}
