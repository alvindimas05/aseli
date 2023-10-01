package helper

import (
	"context"
	"regexp"
	"strings"
	"time"

	// "reflect"

	"github.com/99designs/gqlgen/graphql"
)

func NormalizeQueryResult(data interface{}) interface{} {
	return data.([]interface{})[0].(map[string]interface{})["result"]
}
func ReplaceArrayValue(data []string, original string, replace string) []string {
	index := ArrayIndexOf(original, data)
	if index == -1 {
		return data
	}
	data[index] = replace
	return data
}
func ArrayIndexOf(element string, data []string) (int) {
	for k, v := range data {
		if element == v {
			return k
		}
	}
	return -1    //not found.
 }
func SplitFieldByName(input string, name string) []string {
    parts := strings.Split(input, ",")

    // Create arrays to store parts matching the given name and non-matching parts
    var matchingParts []string
    var nonMatchingParts []string

    for _, part := range parts {
        // Check if the part starts with the given name followed by a dot
        if strings.HasPrefix(part, name+".") {
            matchingParts = append(matchingParts, strings.TrimPrefix(part, name+"."))
        } else {
            nonMatchingParts = append(nonMatchingParts, part)
        }
    }

    // Join the arrays into strings
    matchingString := strings.Join(matchingParts, ",")
    nonMatchingString := strings.Join(nonMatchingParts, ",")

    // Create an array to store the final result
    result := []string{nonMatchingString}
    if matchingString != "" {
        result = append(result, matchingString)
    }

    return result
}
func GetPreloads(ctx context.Context) []string {
	return GetNestedPreloads(
		graphql.GetOperationContext(ctx),
		graphql.CollectFieldsCtx(ctx, nil),
		"",
	)
}
func GetNestedPreloads(ctx *graphql.OperationContext, fields []graphql.CollectedField, prefix string) (preloads []string) {
	for _, column := range fields {
		prefixColumn := GetPreloadString(prefix, column.Name)
		preloads = append(preloads, prefixColumn)
		preloads = append(preloads, GetNestedPreloads(ctx, graphql.CollectFields(ctx, column.Selections, nil), prefixColumn)...)
	}
	return
}
func GetPreloadString(prefix, name string) string {
	if len(prefix) > 0 {
		return prefix + "." + name
	}
	return name
}
func NormalizeFieldsAsArray(ctx context.Context) []string {
	return GetPreloads(ctx)
}
func NormalizeFields(ctx context.Context) string {
	return strings.Join(GetPreloads(ctx), ",")
	// arrFields := graphql.CollectAllFields(ctx)
	// fields := ""
	// for i, field := range arrFields {
	// 	fields += field
	// 	if i < len(arrFields)-1 {
	// 		fields += ","
	// 	}
	// }
	// return fields
}

func SafelyConvertString(input string) string {
    pattern := "[`'\"`]"
    re := regexp.MustCompile(pattern)
    cleaned := re.ReplaceAllString(input, "")
    return cleaned
}

func GetCurrentDateTime() string {
	currentTime := time.Now().UTC()
	return currentTime.Format("2006-01-02T15:04:05Z")
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