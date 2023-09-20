package helper

import (
	"context"
	"regexp"
	// "reflect"

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

func SafelyConvertString(input string) string {
    pattern := "[`'\"`]"
    re := regexp.MustCompile(pattern)
    cleaned := re.ReplaceAllString(input, "")
    return cleaned
}
// func RemoveStructField(inputStruct interface{}, fieldNameToRemove string) interface{} {
//     // Get the type of the input struct
//     inputType := reflect.TypeOf(inputStruct)

//     // Create a new struct type without the specified field
//     var newFields []reflect.StructField
//     for i := 0; i < inputType.NumField(); i++ {
//         field := inputType.Field(i)
//         if field.Name != fieldNameToRemove {
//             newFields = append(newFields, field)
//         }
//     }

//     // Create a new type with the remaining fields
//     newStructType := reflect.StructOf(newFields)

//     // Create a new instance of the modified struct
//     newStructValue := reflect.New(newStructType).Elem()

//     // Copy the values from the original struct to the new one, excluding the removed field
//     originalValue := reflect.ValueOf(inputStruct)
//     for i := 0; i < newStructValue.NumField(); i++ {
//         field := newStructValue.Field(i)
//         fieldName := newFields[i].Name

//         originalField := originalValue.FieldByName(fieldName)
//         if originalField.IsValid() {
//             field.Set(originalField)
//         }
//     }

//     return newStructValue.Interface()
// }